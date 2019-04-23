package org.clulab.linnaeus.model

import org.clulab.wm.eidos.groundings.OntologyBranchNode
import org.clulab.wm.eidos.groundings.OntologyLeafNode
import org.clulab.wm.eidos.groundings.OntologyNode
import org.clulab.wm.eidos.groundings.OntologyRootNode

class EidosNode(id: Int, ontologyNode: OntologyNode) extends DirectedGraphNode[(Int, OntologyNode)]((id, ontologyNode)) {

  def getId: Int = value._1
  def getOntologyNode = value._2

  def ontologyParentNode: Option[OntologyNode] = getOntologyNode match {
    case node: OntologyLeafNode => Option(node.parent)
    case node: OntologyRootNode => None
    case node: OntologyBranchNode => Option(node.parent)
  }

  override def toString: String = getOntologyNode match {
    case node: OntologyLeafNode => node.nodeName
    case node: OntologyRootNode => EidosNode.root
    case node: OntologyBranchNode => node.nodeName
  }
}

object EidosNode {
  var root = "<root>"
  var UN = "/org/clulab/wm/eidos/english/ontologies/un_ontology.yml"

  type Node = EidosNode // DirectedGraphNode[(Int, OntologyNode)]
}
