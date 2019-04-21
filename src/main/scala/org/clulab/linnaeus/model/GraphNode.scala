package org.clulab.linnaeus.model

class GraphNode[T](val data: T) {
  var parents: Seq[GraphNode[T]] = Seq.empty
  var children: Seq[GraphNode[T]] = Seq.empty

  def this(parent: GraphNode[T], data: T) = {
    this(data)
    addParent(parent, true)
  }

  def addParent(parent: GraphNode[T], connect: Boolean = true): Unit = {
    parents = parents :+ parent

    if (connect)
      parent.addChild(this, false)
  }

  def subParent(parent: GraphNode[T], connect: Boolean = true): Unit = {
    if (children.find { node => node.eq(parent) }.nonEmpty ) {
      parents = parents.filter { node => !node.eq(parent) }

      if (connect)
        parent.subChild(this, false)
    }
  }

  def addChild(child: GraphNode[T], connect: Boolean = true): Unit = {
    children = children :+ child

    if (connect)
      child.addParent(this, false)
  }

  def subChild(child: GraphNode[T], connect: Boolean = true): Unit = {
    if (children.find { node => node.eq(child) }.nonEmpty ) {
      children = children.filter { node => !node.eq(child) }

      if (connect)
        child.subParent(this, false)
    }
  }

  def siblings: Seq[GraphNode[T]] = parents.flatMap(_.children)

  def isRoot: Boolean = parents.isEmpty

  def isLeaf: Boolean = children.isEmpty

  def isUnparented: Boolean = isRoot

  def isSingleParented: Boolean = parents.size == 1

  def isMultiParented: Boolean = parents.size > 1

  protected def isTreeOf(parent: GraphNode[T]): Boolean = {
    parents.size == 1 && parents.head.eq(parent) && children.forall { child => child.isTreeOf(this) }
  }

  def isTree: Boolean = {
    parents.isEmpty && children.forall { child => child.isTreeOf(this) }
  }

  protected def mkTreeOf(parent: GraphNode[T]): Unit = {
    if (isSingleParented)
      require(parents.head.eq(parent))
    else {
      require(isMultiParented)

      parents.foreach { otherParent =>
        if (!otherParent.eq(parent))
          otherParent.subChild(this)
      }
      parents = Seq(parent)
    }

    children.foreach { child => child.mkTreeOf(this) }
  }

  def mkTree(): Unit = {
    require(parents.isEmpty)

    children.foreach { child => child.mkTreeOf(this) }
  }

  override def toString = data.toString
}

object GraphNode {

  def reroot[T](nodes: Seq[GraphNode[T]], data: T): GraphNode[T] = {
    val roots = nodes.filter { node => node.isRoot }
    val root = if (roots.size == 1)
      roots.head
    else {
      val newRoot = new GraphNode(data)

      roots.foreach { oldRoot => newRoot.addChild(oldRoot) }
      newRoot
    }

    root
  }
}
