package org.clulab.linnaeus.model

import org.clulab.wm.eidos.groundings.OntologyBranchNode
import org.clulab.wm.eidos.groundings.OntologyLeafNode
import org.clulab.wm.eidos.groundings.OntologyNode
import org.clulab.wm.eidos.groundings.OntologyRootNode

class LinnaeusNode(val id: String) {
  var parent: Option[LinnaeusNode] = None
  var children: Seq[LinnaeusNode] = Seq.empty

  def setParent(linnaeusNode: LinnaeusNode): Boolean = {
    if (parent.isEmpty) {
      parent = Option(linnaeusNode)
      false
    }
    else
      true
  }

  def addChild(linnaeusNode: LinnaeusNode): Unit = {
    children = children :+ linnaeusNode
    linnaeusNode.setParent(this)
  }

  override def toString = id
}
