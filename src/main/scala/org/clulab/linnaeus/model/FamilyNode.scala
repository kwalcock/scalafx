package org.clulab.linnaeus.model

class FamilyNode[T](val name: String, val data: T) {
  var parent: Option[FamilyNode[T]] = None
  var children: Seq[FamilyNode[T]] = Seq.empty

  def getParent: Option[FamilyNode[T]] = parent

  def setParent(familyNode: FamilyNode[T]): Boolean = {
    if (parent.isEmpty) {
      parent = Option(familyNode)
      false
    }
    else
      true
  }

  def addChild(familyNode: FamilyNode[T]): Unit = {
    children = children :+ familyNode
    familyNode.setParent(this)
  }

  override def toString = name
}
