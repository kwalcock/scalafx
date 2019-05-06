package org.clulab.linnaeus.model.graph

import scala.collection.mutable

class GraphNetwork[
  NetworkIdentityType,
  NodeIdentityType, NodeType <: GraphNode[NodeIdentityType],
  EdgeIdentityType, EdgeType <: GraphEdge[EdgeIdentityType]
](val id: NetworkIdentityType)
    extends Identifyable[NetworkIdentityType] {

  protected[this] class NodePacket(val index: Int, val node: NodeType) extends Ordered[NodePacket] {
    /**
      * Only a small number of edges are expected per node, so they are stored in Seqs
      * rather than Maps.
      */
    var incoming: Seq[EdgePacket] = Seq.empty
    var outgoing: Seq[EdgePacket] = Seq.empty

    def isRoot: Boolean = incoming.isEmpty

    def isLeaf: Boolean = outgoing.isEmpty

    def isUnparented: Boolean = isRoot

    def isSingleParented: Boolean = incoming.size == 1

    def isMultiParented: Boolean = incoming.size > 1

    override def compare(that: NodePacket): Int = this.index - that.index

    /**
      * These are kept in "linear" order as received.
      */
    def addIncoming(edgePacket: EdgePacket): Unit = incoming = incoming :+ edgePacket

    def addOutgoing(edgePacket: EdgePacket): Unit = outgoing = outgoing :+ edgePacket

    def subIncoming(edgePacket: EdgePacket): Unit = {
      incoming = incoming.filter { incoming =>
        incoming.edge.getId != edgePacket.edge.getId
      }
    }

    def subOutgoing(edgePacket: EdgePacket): Unit = {
      outgoing = outgoing.filter { outgoing =>
        outgoing.edge.getId != edgePacket.edge.getId
      }
    }
  }

  protected[this] class EdgePacket(val index: Int, val sourcePacket: NodePacket, val edge: EdgeType, val targetPacket: NodePacket)
      extends Ordered[EdgePacket] {
    // Connect the node.
    sourcePacket.addOutgoing(this)
    targetPacket.addIncoming(this)

    override def compare(that: EdgePacket): Int = this.index - that.index
  }

  trait GraphVisitor {
    def foreachNode(f: NodeType => Unit): Unit
    def foreachEdge(f: (NodeType, EdgeType, NodeType) => Unit)
  }

  class ValueGraphVisitor extends GraphVisitor {

    def foreachNode(f: NodeType => Unit): Unit = {
      nodePacketMap.values.foreach { nodePacket =>
        f(nodePacket.node)
      }
    }

    def foreachEdge(f: (NodeType, EdgeType, NodeType) => Unit): Unit = {
      edgePacketMap.values.foreach { edgePacket =>
        f(edgePacket.sourcePacket.node, edgePacket.edge, edgePacket.targetPacket.node)
      }
    }
  }

  class LinearGraphVisitor extends GraphVisitor {
    def foreachNode(f: NodeType => Unit): Unit = {
      nodePacketMap.values.toSeq.sorted.foreach { nodePacket =>
        f(nodePacket.node)
      }
    }

    def foreachEdge(f: (NodeType, EdgeType, NodeType) => Unit): Unit = {
      edgePacketMap.values.toSeq.sorted.foreach { edgePacket =>
        f(edgePacket.sourcePacket.node, edgePacket.edge, edgePacket.targetPacket.node)
      }
    }

    def mapNode[T](f: NodeType => T): Seq[T] = {
      nodePacketMap.values.toSeq.sorted.map { nodePacket =>
        f(nodePacket.node)
      }
    }

    def mapEdge[T](f: (NodeType, EdgeType, NodeType) => T): Seq[T] = {
      edgePacketMap.values.toSeq.sorted.map { edgePacket =>
        f(edgePacket.sourcePacket.node, edgePacket.edge, edgePacket.targetPacket.node)
      }
    }
  }

  class HierarchicalGraphVisitor extends GraphVisitor {
    /**
      * This has an implied top-down order that is only suitable for trees.
      * It will recurse infinitely if there are cycles.
      */
    def foreachNode(f: NodeType => Unit): Unit = {

      def foreachNode(nodePacket: NodePacket): Unit = {
        f(nodePacket.node)
        nodePacket.outgoing.foreach { edgePacket =>
          foreachNode(edgePacket.targetPacket)
        }
      }

      rootPackets.toSeq.sorted.foreach { root =>
        foreachNode(root)
      }
    }

    // This one adds depth which is handy for tabbing.
    def foreachNode(f: (NodeType, Int) => Unit): Unit = {

      def foreachNode(nodePacket: NodePacket, depth: Int): Unit = {
        f(nodePacket.node, depth)
        nodePacket.outgoing.foreach { edgePacket =>
          foreachNode(edgePacket.targetPacket, depth + 1)
        }
      }

      rootPackets.toSeq.sorted.foreach { root =>
        foreachNode(root, 0)
      }
    }

    def foreachEdge(f: (NodeType, EdgeType, NodeType) => Unit): Unit = {

      def foreachEdge(sourcePacket: NodePacket): Unit = {
        sourcePacket.outgoing.foreach { edgePacket =>
          f(sourcePacket.node, edgePacket.edge, edgePacket.targetPacket.node)
          foreachEdge(edgePacket.targetPacket)
        }
      }

      rootPackets.toSeq.sorted.foreach { root =>
        foreachEdge(root)
      }
    }

    def foldDown[T](initial: T)(f: (T, NodeType) => T): T = {

      def foldDown(current: T, nodePacket: NodePacket): T = {
        val result = f(current, nodePacket.node)
        // This calculates the result here and provides the result to
        // the function used for all the children.
        nodePacket.outgoing.foreach { edgePacket =>
          foldDown(result, edgePacket.targetPacket)
        }
        result
      }

      // Should be a tree
      foldDown(initial, rootPackets.head)
    }

    def foldUp[T](f: (NodeType, Seq[T]) => T): T = {

      def foldUp(nodePacket: NodePacket): T = {
        // This calculates the result here and provides the result to
        // the function used for all the children.
        val result = f(nodePacket.node, nodePacket.outgoing.map { edge => foldUp(edge.targetPacket) })

        result
      }

      // Should be a tree
      foldUp(rootPackets.head)
    }
  }

  /**
    * The IdentityTypes here are used to disambiguate the nodes and edges and are suitable map keys.
    */
  protected var nodePacketMap: mutable.Map[NodeIdentityType, NodePacket] = mutable.Map.empty
  protected var edgePacketMap: mutable.Map[EdgeIdentityType, EdgePacket] = mutable.Map.empty
  /**
    * These Indexers allow the nodes and edges to be sorted in the order added (newed).
    * This is especially useful when rewriting a network so that before and after can be compared.
    */
  protected var nodePacketIndexer: Indexer = new Indexer()
  protected var edgePacketIndexer: Indexer = new Indexer()

  def getId: NetworkIdentityType = id

  /**
    * These return Packets so that the actual node does not have to be looked up
    * in the map when it is used.  It is an "optimization".
    */
  def addNode(node: NodeType): Unit = {
    require(!nodePacketMap.contains(node.getId))
    val nodePacket = new NodePacket(nodePacketIndexer.next, node)
    addNode(nodePacket)
  }

  protected def addNode(nodePacket: NodePacket): NodePacket = {
    nodePacketMap += nodePacket.node.getId -> nodePacket
    nodePacket
  }

  def addEdge(sourceId: NodeIdentityType, edge: EdgeType, targetId: NodeIdentityType): Unit = {
    require(nodePacketMap.contains(sourceId))
    require(!edgePacketMap.contains(edge.getId))
    require(nodePacketMap.contains(targetId))

    val sourcePacket = nodePacketMap(sourceId)
    val targetPacket = nodePacketMap(targetId)

    addEdge(sourcePacket, edge, targetPacket)
  }

  protected def addEdge(sourcePacket: NodePacket, edge: EdgeType, targetPacket: NodePacket): EdgePacket = {
    val edgePacket = new EdgePacket(edgePacketIndexer.next, sourcePacket, edge, targetPacket)

    edgePacketMap += edge.getId -> edgePacket
    edgePacket
  }

  protected def rootPackets: Iterable[NodePacket] = nodePacketMap.values.filter(_.isRoot)

  def rootNode: NodeType = {
    require(isTree)
    rootPackets.head.node
  }

  // Also not circular!
  def isTree: Boolean = rootPackets.size == 1 && !nodePacketMap.values.exists(_.isMultiParented)

  def newRootNode(): NodeType = ???

  def newRootEdge(): EdgeType = ???

  def mkTree(): Unit = {

    def foreachNodePacket(f: NodePacket => Unit): Unit = {
      nodePacketMap.values.toSeq.sorted.foreach { nodePacket =>
        f(nodePacket)
      }
    }

    // Remove edges that result in any node having multiple parents.
    foreachNodePacket { nodePacket =>
      if (nodePacket.incoming.size > 1) {
        nodePacket.incoming.tail.foreach { edgePacket =>
          // Remove the outgoing edge from the parent
          edgePacket.sourcePacket.subOutgoing(edgePacket)
          // Remove the outgoing edge from the network
          edgePacketMap -= edgePacket.edge.getId
        }
        // Remove all but first incoming edges from the child.
        nodePacket.incoming = Seq(nodePacket.incoming.head)
      }
    }

    val oldRootPackets = rootPackets
    val rootPacket = new NodePacket(nodePacketIndexer.next, newRootNode())

    addNode(rootPacket)
    oldRootPackets.foreach { oldRootPacket =>
      addEdge(rootPacket, newRootEdge(), oldRootPacket)
    }
  }
}