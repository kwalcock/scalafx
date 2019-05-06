package org.clulab.linnaeus.model

import org.clulab.linnaeus.model.graph.eidos.EidosNetwork
import org.clulab.linnaeus.model.graph.eidos.EidosNode
import org.clulab.linnaeus.model.reader.EidosReader
import scalafx.scene.control.TreeItem

import scala.annotation.tailrec
import scala.collection.mutable
import scala.util.Random

object TreeNode {
  protected val ONTOLOGY_PATH =  "data/un_ontology.yml"
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

  class NodeItem(val node: EidosNode) extends TreeItem[String](node.name) {
    expanded = true

    if (node.descriptions.nonEmpty) {
      val descriptionsItem = new DescriptionsItem
      node.descriptions.foreach { description =>
        descriptionsItem.children.add(new DescriptionItem(description))
      }
      children.add(descriptionsItem)
    }

    if (node.examples.nonEmpty) {
      val examplesItem = new ExamplesItem
      node.examples.foreach { example =>
        examplesItem.children.add(new ExampleItem(example))
      }
      children.add(examplesItem)
    }

    if (node.patterns.nonEmpty) {
      val patternsItem = new PatternsItem
      node.patterns.foreach { pattern =>
        patternsItem.children.add(new ExampleItem(pattern.toString))
      }
      children.add(patternsItem)
    }
  }

  protected def mkTree(network: EidosNetwork): TreeItem[String] = {
    val visitor = new network.HierarchicalGraphVisitor()

    visitor.foldDown(Option.empty[NodeItem]) { (parentNodeItemOpt: Option[NodeItem], node) =>
      val result = new NodeItem(node)

      parentNodeItemOpt.foreach { parentNodeItem =>
        parentNodeItem.children.add(result)
      }
      Some(result)
    }.get
  }

  def fromEidos: TreeItem[String] = {
    val network = new EidosReader(ONTOLOGY_PATH).read()
    val rootItem = mkTree(network)

    rootItem
  }
}
