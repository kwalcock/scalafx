package org.clulab.linnaeus.model

import java.util.IdentityHashMap

class DirectedGraphNode[T](data: T) extends GraphNode[T](data) {
  var parents: Seq[DirectedGraphNode[T]] = Seq.empty
  var children: Seq[DirectedGraphNode[T]] = Seq.empty

  def this(parent: DirectedGraphNode[T], data: T) = {
    this(data)
    addParent(parent)
  }

  // Return true to stop
  type foreachPredicate = DirectedGraphNode[T] => Boolean

  def addParent(parent: DirectedGraphNode[T], connect: Boolean = true): Unit = {
    parents = parents :+ parent

    if (connect)
      parent.addChild(this, connect = false)
  }

  def subParent(parent: DirectedGraphNode[T], connect: Boolean = true): Unit = {
    if (children.exists { node => node.eq(parent) }) {
      parents = parents.filter { node => !node.eq(parent) }

      if (connect)
        parent.subChild(this, connect = false)
    }
  }

  def addChild(child: DirectedGraphNode[T], connect: Boolean = true): Unit = {
    children = children :+ child

    if (connect)
      child.addParent(this, connect = false)
  }

  def subChild(child: DirectedGraphNode[T], connect: Boolean = true): Unit = {
    if (children.exists { node => node.eq(child) }) {
      children = children.filter { node => !node.eq(child) }

      if (connect)
        child.subParent(this, connect = false)
    }
  }

  def siblings: Seq[DirectedGraphNode[T]] = parents.flatMap(_.children)

  def isRoot: Boolean = parents.isEmpty

  def isLeaf: Boolean = children.isEmpty

  def isUnparented: Boolean = isRoot

  def isSingleParented: Boolean = parents.size == 1

  def isMultiParented: Boolean = parents.size > 1

  protected def isTreeOf(parent: DirectedGraphNode[T]): Boolean = {
    parents.size == 1 && parents.head.eq(parent) && children.forall { child => child.isTreeOf(this) }
  }

  def isTree: Boolean = {
    parents.isEmpty && children.forall { child => child.isTreeOf(this) }
  }

  protected def mkTreeOf(parent: DirectedGraphNode[T]): Unit = {
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

  override def toString: String = data.toString

  protected def foreach(nodeMap: IdentityHashMap[DirectedGraphNode[T], Int], f: foreachPredicate, meFirst: Boolean): Boolean = {
    def protectedF(node: DirectedGraphNode[T]): Boolean = {
      if (nodeMap.containsKey(node))
        false
      else {
        nodeMap.put(node, 1)
        f(node)
      }
    }

    def protectedForeach(node: DirectedGraphNode[T]): Boolean = {
      if (nodeMap.containsKey(node))
        false
      else {
        nodeMap.put(node, 1)
        node.foreach(nodeMap, f, meFirst)
      }
    }

    if (meFirst)
      protectedF(this) || children.exists { child => protectedForeach(child) }
    else
      children.exists { child => protectedForeach(child) } || protectedF(this)
  }

  def foreach(f: foreachPredicate, meFirst: Boolean = true): Boolean = {
    val nodeMap: IdentityHashMap[DirectedGraphNode[T], Int] = new IdentityHashMap()

    foreach(nodeMap, f, meFirst)
  }

  def hasLoop: Boolean = {
    val nodeMap: IdentityHashMap[DirectedGraphNode[T], Int] = new IdentityHashMap()

    def hasLoop(node: DirectedGraphNode[T]): Boolean = {
      nodeMap.containsKey(node) || {
        nodeMap.put(node, 1)
        false
      }
    }

    foreach(hasLoop)
  }
}

object DirectedGraphNode {

  def reroot[T](nodes: Seq[DirectedGraphNode[T]], data: T): DirectedGraphNode[T] = {
    val roots = nodes.filter { node => node.isRoot }
    val root = if (roots.size == 1)
      roots.head
    else {
      val newRoot = new DirectedGraphNode(data)

      roots.foreach { oldRoot => newRoot.addChild(oldRoot) }
      newRoot
    }

    root
  }
}
