package org.clulab.linnaeus.model

import org.clulab.wm.eidos.EidosSystem
import org.clulab.wm.eidos.groundings.OntologyBranchNode
import org.clulab.wm.eidos.groundings.OntologyLeafNode
import org.clulab.wm.eidos.groundings.OntologyNode
import org.clulab.wm.eidos.groundings.OntologyRootNode
import org.clulab.wm.eidos.groundings.TreeDomainOntology.TreeDomainOntologyBuilder

import scala.annotation.tailrec
import scala.collection.mutable

case class OntologyTreeItem(ontologyNode: OntologyNode, var parent: Option[OntologyTreeItem] = None,
  var children: Seq[OntologyTreeItem] = Seq.empty) {

  def ontologyParentNode: Option[OntologyNode] = ontologyNode match {
    case node: OntologyLeafNode => Option(node.parent)
    case node: OntologyRootNode => None
    case node: OntologyBranchNode => Option(node.parent)
  }

  override def toString: String = ontologyNode match {
    case node: OntologyLeafNode => node.nodeName
    case node: OntologyRootNode => "<root>"
    case node: OntologyBranchNode => node.nodeName
  }
}

object OntologyTree {
  protected val ONTOLOGY_PATH =  "/org/clulab/wm/eidos/english/ontologies/un_ontology.yml"

  @tailrec
  protected final def mkTree(treeItems: Seq[OntologyTreeItem], ontologyParentNodeToOntologyTreeItemMap: mutable.Map[OntologyNode, OntologyTreeItem]): Unit = {
    val newParentItems = treeItems.flatMap { ontologyTreeItem =>
      val ontologyParentNodeOpt = ontologyTreeItem.ontologyParentNode
      val parentItemOpt = ontologyParentNodeOpt.flatMap { ontologyParentNode =>
        val parentItem = ontologyParentNodeToOntologyTreeItemMap.getOrElseUpdate(ontologyParentNode, OntologyTreeItem(ontologyParentNode))
        val isNewParentItem = parentItem.children.isEmpty

        parentItem.children = parentItem.children :+ ontologyTreeItem
        if (isNewParentItem) Some(parentItem)
        else None
      }

      parentItemOpt
    }

    if (newParentItems.nonEmpty)
      mkTree(newParentItems, ontologyParentNodeToOntologyTreeItemMap)
  }

  def mkTree(): OntologyTreeItem = {
    val eidosSystem = new EidosSystem()
    val proc = eidosSystem.proc
    val canonicalizer = eidosSystem.canonicalizer
    val filter = true
    val treeDomainOntology = new TreeDomainOntologyBuilder(proc, canonicalizer, filter).buildFromPath(ONTOLOGY_PATH)
    val ontologyTreeItems = treeDomainOntology.ontologyNodes.map(OntologyTreeItem(_))
    val ontologyParentNodeToOntologyTreeItemMap = mutable.Map.empty[OntologyNode, OntologyTreeItem]

    val newParentItems = ontologyTreeItems.flatMap { ontologyTreeItem =>
      val ontologyNode = ontologyTreeItem.ontologyParentNode.get
      val parentItem = ontologyParentNodeToOntologyTreeItemMap.getOrElseUpdate(ontologyNode, OntologyTreeItem(ontologyNode))
      val isNewParentItem = parentItem.children.isEmpty

      parentItem.children = parentItem.children :+ ontologyTreeItem
      if (isNewParentItem) Some(parentItem)
      else None
    }
    mkTree(newParentItems, ontologyParentNodeToOntologyTreeItemMap)

    val ontologyParentNodeToParentItemOpt = ontologyParentNodeToOntologyTreeItemMap.find { case (ontologyParentNode, _) =>
      ontologyParentNode.isRoot
    }
    ontologyParentNodeToParentItemOpt.get._2.children.head
  }
}
