package org.clulab.linnaeus.model.graph

import scala.collection.mutable

class GraphNetwork[
  NetworkIdentityType,
  NodeIdentityType, NodeType <: GraphNode[NodeIdentityType],
  EdgeIdentityType, EdgeType <: GraphEdge[EdgeIdentityType]
](val id: NetworkIdentityType)
    extends Identifyable[NetworkIdentityType] {

  class NodePacket(val index: Int, val node: NodeType) extends Ordered[NodePacket] {
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

  class EdgePacket(val index: Int, val sourcePacket: NodePacket, val edge: EdgeType, val targetPacket: NodePacket)
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

    def foreachNodePacket(f: NodePacket => Unit): Unit = {
      nodePacketMap.values.toSeq.sorted.foreach { nodePacket =>
        f(nodePacket)
      }
    }

    def mapNode[T](f: NodeType => T): Seq[T] = {
      nodePacketMap.values.toSeq.sorted.map { nodePacket =>
        f(nodePacket.node)
      }
    }

    def foreachEdge(f: (NodeType, EdgeType, NodeType) => Unit): Unit = {
      edgePacketMap.values.toSeq.sorted.foreach { edgePacket =>
        f(edgePacket.sourcePacket.node, edgePacket.edge, edgePacket.targetPacket.node)
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

      def foreachNodeInHierarchicalOrder(nodePacket: NodePacket): Unit = {
        f(nodePacket.node)
        nodePacket.outgoing.foreach { edgePacket =>
          foreachNodeInHierarchicalOrder(edgePacket.targetPacket)
        }
      }

      rootPackets.toSeq.sorted.foreach { root =>
        foreachNodeInHierarchicalOrder(root)
      }
    }

    def foreachEdge(f: (NodeType, EdgeType, NodeType) => Unit): Unit = ???
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
  def newNodePacket(node: NodeType): NodePacket = {
    require(!nodePacketMap.contains(node.getId))
    val nodePacket = new NodePacket(nodePacketIndexer.next, node)
    nodePacketMap += node.getId -> nodePacket
    nodePacket
  }

  def newEdge(sourcePacket: NodePacket, edge: EdgeType, targetPacket: NodePacket): EdgePacket = {
    require(nodePacketMap.contains(sourcePacket.node.getId))
    require(!edgePacketMap.contains(edge.getId))
    require(nodePacketMap.contains(targetPacket.node.getId))
    val edgePacket = new EdgePacket(edgePacketIndexer.next, sourcePacket, edge, targetPacket)
    edgePacketMap += edge.getId -> edgePacket
    edgePacket
  }

  def getNodePacket(nodeId: NodeIdentityType): Option[NodePacket] = nodePacketMap.get(nodeId)

  def getEdgePacket(edgeId: EdgeIdentityType): Option[EdgePacket] = edgePacketMap.get(edgeId)

  def rootPackets: Iterable[NodePacket] = nodePacketMap.values.filter(_.isRoot)

  def leafPackets: Iterable[NodePacket] = nodePacketMap.values.filter(_.isLeaf)

  def rootPacket: NodePacket = {
    require(isTree)
    rootPackets.head
  }
  // Also not circular!
  def isTree: Boolean = rootPackets.size == 1 && !nodePacketMap.values.exists(_.isMultiParented)

  def newRootNode(): NodeType = ???

  def newRootEdge(): EdgeType = ???

  def mkTree(): Unit = {
    val visitor = new LinearGraphVisitor()

    // Remove edges that result in any node having multiple parents.
    visitor.foreachNodePacket { nodePacket =>
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
    val rootNodePacket = newNodePacket(newRootNode())

    oldRootPackets.foreach { oldRootPacket =>
      newEdge(rootNodePacket, newRootEdge(), oldRootPacket)
    }
  }
}