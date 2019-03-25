package org.clulab.linnaeus.model

import org.clulab.wm.eidos.EidosSystem
import org.clulab.wm.eidos.groundings.OntologyBranchNode
import org.clulab.wm.eidos.groundings.OntologyLeafNode
import org.clulab.wm.eidos.groundings.OntologyNode
import org.clulab.wm.eidos.groundings.OntologyParentNode
import org.clulab.wm.eidos.groundings.OntologyRootNode
import org.clulab.wm.eidos.groundings.TreeDomainOntology.TreeDomainOntologyBuilder
import scalafx.scene.control.TreeItem

import scala.annotation.tailrec
import scala.collection.mutable
import scala.util.Random

object TreeNode {
  protected val ONTOLOGY_PATH =  "/org/clulab/wm/eidos/english/ontologies/un_ontology.yml"
  protected val rng = new Random()

  protected def randomWord(): String = {
    val chars = 'a' to 'z'
    val length = rng.nextInt(10) + 1
    val stringBuilder = new StringBuilder

    0.until(length).foreach { _ => stringBuilder.append(chars(rng.nextInt(chars.length))) }
    stringBuilder.toString
  }

  protected def innerRandom(levelsRemaining: Int, countRemaining: Int): (TreeItem[String], Int) = {
    val value = randomWord
    val treeItem = new TreeItem(value)
    treeItem.expanded = true
    var countUsed = 1
    val length = rng.nextInt(10) + 1

    0.until(length).foreach { index =>
      if (0 < levelsRemaining && countUsed < countRemaining) {
        val (child, used) = innerRandom(levelsRemaining - 1, countRemaining - countUsed)

        treeItem.children.add(child)
        countUsed += used
      }
    }
    (treeItem, countUsed)
  }

  def random(levels: Int, count: Int): TreeItem[String] = {
    var (treeItem, countUsed) = innerRandom(levels, count)
    println("Count used: " + countUsed)
    treeItem
  }


  class DescriptionItem(description: String) extends TreeItem[String](description)

  class DescriptionsItem extends TreeItem[String]("Descriptions") {
    expanded = true
  }

  class ExampleItem(example: String) extends TreeItem[String](example)

  class ExamplesItem extends TreeItem[String]("Examples") {
    expanded = true
  }

  class PatternItem(pattern: String) extends TreeItem[String](pattern)

  class PatternsItem extends TreeItem[String]("Patterns") {
    expanded = true
  }

  class LeafItem(val ontologyLeafNode: OntologyLeafNode) extends TreeItem[String](ontologyLeafNode.nodeName) {
    expanded = true

    if (ontologyLeafNode.descriptions.isDefined) {
      val descriptionsItem = new DescriptionsItem
      ontologyLeafNode.descriptions.foreach { descriptions =>
        descriptions.foreach { description =>
          descriptionsItem.children.add(new DescriptionItem(description))
        }
      }
      children.add(descriptionsItem)
    }

    if (ontologyLeafNode.examples.isDefined) {
      val nonEmptyExamples = ontologyLeafNode.examples.get.filter(_.nonEmpty)

      if (nonEmptyExamples.nonEmpty) {
        val examplesItem = new ExamplesItem
        nonEmptyExamples.map { example =>
          examplesItem.children.add(new ExampleItem(example))
        }
        children.add(examplesItem)
      }
    }

    if (ontologyLeafNode.patterns.isDefined) {
      val patternsItem = new PatternsItem
      ontologyLeafNode.patterns.foreach { patterns =>
        patterns.foreach { pattern =>
          patternsItem.children.add(new ExampleItem(pattern.toString))
        }
      }
      children.add(patternsItem)
    }
  }

  protected def getNodeName(ontologyParentNode: OntologyParentNode): String =
      if (ontologyParentNode.isRoot) "<root>"
      else ontologyParentNode.asInstanceOf[OntologyBranchNode].nodeName

  protected def getOntologyParentNode(ontologyParentNode: OntologyParentNode): Option[OntologyParentNode] =
    if (ontologyParentNode.isRoot) None
    else Some(ontologyParentNode.asInstanceOf[OntologyBranchNode].parent)

  class ParentItem(val ontologyParentNode: OntologyParentNode) extends TreeItem[String](getNodeName(ontologyParentNode)) {
    expanded = true
  }

  @tailrec
  protected def mkTree(treeItems: Seq[ParentItem], ontologyParentNodeToParentItemMap: mutable.Map[OntologyParentNode, ParentItem]): Unit = {
    val newParentItems = treeItems.flatMap { treeItem =>
      val ontologyParentNodeOpt = getOntologyParentNode(treeItem.ontologyParentNode)
      val parentItemOpt = ontologyParentNodeOpt.flatMap { ontologyParentNode =>
        val parentItem = ontologyParentNodeToParentItemMap.getOrElseUpdate(ontologyParentNode, new ParentItem(ontologyParentNode))
        val isNewParentItem = parentItem.children.isEmpty

        parentItem.children.add(treeItem)
        if (isNewParentItem) Some(parentItem)
        else None
      }

      parentItemOpt
    }

    if (newParentItems.nonEmpty)
      mkTree(newParentItems, ontologyParentNodeToParentItemMap)
  }

  protected def mkTree(treeItems: Seq[LeafItem]): TreeItem[String] = {
    val ontologyParentNodeToParentItemMap = mutable.Map.empty[OntologyParentNode, ParentItem]
    val newParentItems = treeItems.flatMap { treeItem =>
      val ontologyParentNode = treeItem.ontologyLeafNode.parent
      val parentItem = ontologyParentNodeToParentItemMap.getOrElseUpdate(ontologyParentNode, new ParentItem(ontologyParentNode))
      val isNewParentItem = parentItem.children.isEmpty

      parentItem.children.add(treeItem)
      if (isNewParentItem) Some(parentItem)
      else None
    }
    mkTree(newParentItems, ontologyParentNodeToParentItemMap)

    val ontologyParentNodeToParentItemOpt = ontologyParentNodeToParentItemMap.find { case (ontologyParentNode, parentItem) =>
      ontologyParentNode.isRoot
    }
    ontologyParentNodeToParentItemOpt.get._2
  }

  def fromEidos: TreeItem[String] = {
    val eidosSystem = new EidosSystem()
    val proc = eidosSystem.proc
    val canonicalizer = eidosSystem.canonicalizer
    val filter = true
    val treeDomainOntology = new TreeDomainOntologyBuilder(proc, canonicalizer, filter).buildFromPath(ONTOLOGY_PATH)
    val leafItems = treeDomainOntology.ontologyNodes.map {new LeafItem(_)} // need to find single root
    val rootItem = mkTree(leafItems)

    rootItem
  }
}
