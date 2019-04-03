package org.clulab.linnaeus.model

import org.clulab.wm.eidos.groundings.OntologyBranchNode
import org.clulab.wm.eidos.groundings.OntologyLeafNode
import org.clulab.wm.eidos.groundings.OntologyNode
import org.clulab.wm.eidos.groundings.OntologyRootNode

case class OntologyTreeItem(id: Int, ontologyNode: OntologyNode, var parent: Option[OntologyTreeItem] = None,
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
