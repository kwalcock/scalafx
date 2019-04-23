package org.clulab.linnaeus.model.reader

import org.clulab.linnaeus.model.EidosNode
import org.clulab.wm.eidos.EidosSystem
import org.clulab.wm.eidos.groundings.OntologyNode
import org.clulab.wm.eidos.groundings.TreeDomainOntology.TreeDomainOntologyBuilder

import scala.annotation.tailrec
import scala.collection.mutable

object EidosReader {

  protected def indexer(start: Int): () => Int = {
    var value = start

    def next(): Int = {
      val result = value
      value += 1
      result
    }
    next
  }

  @tailrec
  protected final def read(nextId: () => Int, treeItems: Seq[EidosNode], ontologyParentNodeToOntologyTreeItemMap: mutable.Map[OntologyNode, EidosNode]): Unit = {
    val newParentItems = treeItems.flatMap { eidosNode =>
      val ontologyParentNodeOpt = eidosNode.ontologyParentNode
      val parentItemOpt = ontologyParentNodeOpt.flatMap { ontologyParentNode =>
        val parentItem = ontologyParentNodeToOntologyTreeItemMap.getOrElseUpdate(ontologyParentNode, new EidosNode.Node(nextId(), ontologyParentNode))
        val isNewParentItem = parentItem.children.isEmpty

        parentItem.children = parentItem.children :+ eidosNode
        if (isNewParentItem) Some(parentItem)
        else None
      }

      parentItemOpt
    }

    if (newParentItems.nonEmpty)
      read(nextId, newParentItems, ontologyParentNodeToOntologyTreeItemMap)
  }

  def read(path: String): EidosNode.Node = {
    val nextId = indexer(0)
    val eidosSystem = new EidosSystem()
    val proc = eidosSystem.proc
    val canonicalizer = eidosSystem.canonicalizer
    val filter = true
    val treeDomainOntology = new TreeDomainOntologyBuilder(proc, canonicalizer, filter).buildFromPath(path)
    val eidosNodes = treeDomainOntology.ontologyNodes.map(new EidosNode.Node(nextId(), _))
    val ontologyNodeToEidosNodeMap = mutable.Map.empty[OntologyNode, EidosNode.Node]


    val newParentItems = eidosNodes.flatMap { ontologyTreeItem =>
      val ontologyNode = ontologyTreeItem.ontologyParentNode.get
      val parentItem = ontologyNodeToEidosNodeMap.getOrElseUpdate(ontologyNode, new EidosNode.Node(nextId(), ontologyNode))
      val isNewParentItem = parentItem.children.isEmpty

      parentItem.children = parentItem.children :+ ontologyTreeItem
      if (isNewParentItem) Some(parentItem)
      else None
    }
    read(nextId, newParentItems, ontologyNodeToEidosNodeMap)

    val ontologyNodeToEidosNodeOpt = ontologyNodeToEidosNodeMap.find { case (ontologyParentNode, _) =>
      ontologyParentNode.isRoot
    }
    ontologyNodeToEidosNodeOpt.get._2.children.head.asInstanceOf[EidosNode.Node]
  }
}
