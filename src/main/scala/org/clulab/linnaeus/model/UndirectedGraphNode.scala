package org.clulab.linnaeus.model

class UndirectedGraphNode[T](data: T) extends GraphNode[T](data) {
  var neighbors: Seq[UndirectedGraphNode[T]] = Seq.empty

  def this(neighbor: UndirectedGraphNode[T], data: T) = {
    this(data)
    addNeighbor(neighbor)
  }

  def addNeighbor(neighbor: UndirectedGraphNode[T], connect: Boolean = true): Unit = {
    neighbors = neighbors :+ neighbor

    if (connect)
      neighbor.addNeighbor(this, connect = false)
  }

  def subNeighbor(neighbor: UndirectedGraphNode[T], connect: Boolean = true): Unit = {
    if (neighbors.exists { node => node.eq(neighbor) }) {
      neighbors = neighbors.filter { node => !node.eq(neighbor) }

      if (connect)
        neighbor.subNeighbor(this, connect = false)
    }
  }
}


