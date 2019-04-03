package org.clulab.linnaeus.model.reader

import org.clulab.linnaeus.model.OntologyTreeItem
import org.clulab.wm.eidos.EidosSystem
import org.clulab.wm.eidos.groundings.OntologyNode
import org.clulab.wm.eidos.groundings.TreeDomainOntology.TreeDomainOntologyBuilder

import scala.annotation.tailrec
import scala.collection.mutable

object EidosReader {
  protected val ONTOLOGY_PATH =  "/org/clulab/wm/eidos/english/ontologies/un_ontology.yml"

  protected def indexer(start: Int): () => Int = {
    var value = start

    def next(): Int = {
      val result = value
      value += 1
      result
    }
    next _
  }

  @tailrec
  protected final def read(nextId: () => Int, treeItems: Seq[OntologyTreeItem], ontologyParentNodeToOntologyTreeItemMap: mutable.Map[OntologyNode, OntologyTreeItem]): Unit = {
    val newParentItems = treeItems.flatMap { ontologyTreeItem =>
      val ontologyParentNodeOpt = ontologyTreeItem.ontologyParentNode
      val parentItemOpt = ontologyParentNodeOpt.flatMap { ontologyParentNode =>
        val parentItem = ontologyParentNodeToOntologyTreeItemMap.getOrElseUpdate(ontologyParentNode, OntologyTreeItem(nextId(), ontologyParentNode))
        val isNewParentItem = parentItem.children.isEmpty

        parentItem.children = parentItem.children :+ ontologyTreeItem
        if (isNewParentItem) Some(parentItem)
        else None
      }

      parentItemOpt
    }

    if (newParentItems.nonEmpty)
      read(nextId, newParentItems, ontologyParentNodeToOntologyTreeItemMap)
  }

  def read(): OntologyTreeItem = {
    val nextId = indexer(0)
    val eidosSystem = new EidosSystem()
    val proc = eidosSystem.proc
    val canonicalizer = eidosSystem.canonicalizer
    val filter = true
    val treeDomainOntology = new TreeDomainOntologyBuilder(proc, canonicalizer, filter).buildFromPath(ONTOLOGY_PATH)
    val ontologyTreeItems = treeDomainOntology.ontologyNodes.map(OntologyTreeItem(nextId(), _))
    val ontologyParentNodeToOntologyTreeItemMap = mutable.Map.empty[OntologyNode, OntologyTreeItem]


    val newParentItems = ontologyTreeItems.flatMap { ontologyTreeItem =>
      val ontologyNode = ontologyTreeItem.ontologyParentNode.get
      val parentItem = ontologyParentNodeToOntologyTreeItemMap.getOrElseUpdate(ontologyNode, OntologyTreeItem(nextId(), ontologyNode))
      val isNewParentItem = parentItem.children.isEmpty

      parentItem.children = parentItem.children :+ ontologyTreeItem
      if (isNewParentItem) Some(parentItem)
      else None
    }
    read(nextId, newParentItems, ontologyParentNodeToOntologyTreeItemMap)

    val ontologyParentNodeToParentItemOpt = ontologyParentNodeToOntologyTreeItemMap.find { case (ontologyParentNode, _) =>
      ontologyParentNode.isRoot
    }
    ontologyParentNodeToParentItemOpt.get._2.children.head
  }
}
