package org.clulab.linnaeus.model.graph

import scala.collection.mutable

class GraphNetwork[
  NetworkIdentityType,
  NodeIdentityType, NodeType <: GraphNode[NodeIdentityType],
  EdgeIdentityType, EdgeType <: GraphEdge[EdgeIdentityType]
](val id: NetworkIdentityType)
    extends Identifyable[NetworkIdentityType] {

  class NodeRecord(val index: Int, val node: NodeType) extends Ordered[NodeRecord] {
    /**
      * Only a small number of edges are expected per node, so they are stored in Seqs
      * rather than Maps.
      */
    var incoming: Seq[EdgeRecord] = Seq.empty
    var outgoing: Seq[EdgeRecord] = Seq.empty

    def isRoot: Boolean = incoming.isEmpty

    def isLeaf: Boolean = outgoing.isEmpty

    def isUnparented: Boolean = isRoot

    def isSingleParented: Boolean = incoming.size == 1

    def isMultiParented: Boolean = incoming.size > 1

    override def compare(that: NodeRecord): Int = this.index - that.index

    /**
      * These are kept in "linear" order as received.
      */
    def addIncoming(edgeRecord: EdgeRecord): Unit = incoming = incoming :+ edgeRecord

    def addOutgoing(edgeRecord: EdgeRecord): Unit = outgoing = outgoing :+ edgeRecord

    def subIncoming(edgeRecord: EdgeRecord): Unit = {
      incoming = incoming.filter { incoming =>
        incoming.edge.getId != edgeRecord.edge.getId
      }
    }

    def subOutgoing(edgeRecord: EdgeRecord): Unit = {
      outgoing = outgoing.filter { outgoing =>
        outgoing.edge.getId != edgeRecord.edge.getId
      }
    }
  }

  class EdgeRecord(val index: Int, val sourceRecord: NodeRecord, val edge: EdgeType, val targetRecord: NodeRecord)
      extends Ordered[EdgeRecord] {
    // Connect the node.
    sourceRecord.addOutgoing(this)
    targetRecord.addIncoming(this)

    override def compare(that: EdgeRecord): Int = this.index - that.index
  }

  trait GraphVisitor {
    def foreachNode(f: NodeType => Unit): Unit
    def foreachEdge(f: (NodeType, EdgeType, NodeType) => Unit)
  }

  class ValueGraphVisitor extends GraphVisitor {

    def foreachNode(f: NodeType => Unit): Unit = {
      nodeRecordMap.values.foreach { nodeRecord =>
        f(nodeRecord.node)
      }
    }

    def foreachEdge(f: (NodeType, EdgeType, NodeType) => Unit): Unit = {
      edgeRecordMap.values.foreach { edgeRecord =>
        f(edgeRecord.sourceRecord.node, edgeRecord.edge, edgeRecord.targetRecord.node)
      }
    }
  }

  class LinearGraphVisitor extends GraphVisitor {
    def foreachNode(f: NodeType => Unit): Unit = {
      nodeRecordMap.values.toSeq.sorted.foreach { nodeRecord =>
        f(nodeRecord.node)
      }
    }

    def foreachNodeRecord(f: NodeRecord => Unit): Unit = {
      nodeRecordMap.values.toSeq.sorted.foreach { nodeRecord =>
        f(nodeRecord)
      }
    }

    def mapNode[T](f: NodeType => T): Seq[T] = {
      nodeRecordMap.values.toSeq.sorted.map { nodeRecord =>
        f(nodeRecord.node)
      }
    }

    def foreachEdge(f: (NodeType, EdgeType, NodeType) => Unit): Unit = {
      edgeRecordMap.values.toSeq.sorted.foreach { edgeRecord =>
        f(edgeRecord.sourceRecord.node, edgeRecord.edge, edgeRecord.targetRecord.node)
      }
    }

    def mapEdge[T](f: (NodeType, EdgeType, NodeType) => T): Seq[T] = {
      edgeRecordMap.values.toSeq.sorted.map { edgeRecord =>
        f(edgeRecord.sourceRecord.node, edgeRecord.edge, edgeRecord.targetRecord.node)
      }
    }
  }

  class HierarchicalGraphVisitor extends GraphVisitor {
    /**
      * This has an implied top-down order that is only suitable for trees.
      * It will recurse infinitely if there are cycles.
      */
    def foreachNode(f: NodeType => Unit): Unit = {

      def foreachNodeInHierarchicalOrder(nodeRecord: NodeRecord): Unit = {
        f(nodeRecord.node)
        nodeRecord.outgoing.foreach { edgeRecord =>
          foreachNodeInHierarchicalOrder(edgeRecord.targetRecord)
        }
      }

      rootRecords.toSeq.sorted.foreach { root =>
        foreachNodeInHierarchicalOrder(root)
      }
    }

    def foreachEdge(f: (NodeType, EdgeType, NodeType) => Unit): Unit = ???
  }

  /**
    * The IdentityTypes here are used to disambiguate the nodes and edges and are suitable map keys.
    */
  protected var nodeRecordMap: mutable.Map[NodeIdentityType, NodeRecord] = mutable.Map.empty
  protected var edgeRecordMap: mutable.Map[EdgeIdentityType, EdgeRecord] = mutable.Map.empty
  /**
    * These Indexers allow the nodes and edges to be sorted in the order added (newed).
    * This is especially useful when rewriting a network so that before and after can be compared.
    */
  protected var nodeRecordIndexer: Indexer = new Indexer()
  protected var edgeRecordIndexer: Indexer = new Indexer()

  def getId: NetworkIdentityType = id

  /**
    * These return Records so that the actual node does not have to be looked up
    * in the map when it is used.  It is an "optimization".
    */
  def newNodeRecord(node: NodeType): NodeRecord = {
    require(!nodeRecordMap.contains(node.getId))
    val nodeRecord = new NodeRecord(nodeRecordIndexer.next, node)
    nodeRecordMap += node.getId -> nodeRecord
    nodeRecord
  }

  def newEdge(sourceRecord: NodeRecord, edge: EdgeType, targetRecord: NodeRecord): EdgeRecord = {
    require(nodeRecordMap.contains(sourceRecord.node.getId))
    require(!edgeRecordMap.contains(edge.getId))
    require(nodeRecordMap.contains(targetRecord.node.getId))
    val edgeRecord = new EdgeRecord(edgeRecordIndexer.next, sourceRecord, edge, targetRecord)
    edgeRecordMap += edge.getId -> edgeRecord
    edgeRecord
  }

  def getNodeRecord(nodeId: NodeIdentityType): Option[NodeRecord] = nodeRecordMap.get(nodeId)

  def getEdgeRecord(edgeId: EdgeIdentityType): Option[EdgeRecord] = edgeRecordMap.get(edgeId)

  def rootRecords: Iterable[NodeRecord] = nodeRecordMap.values.filter(_.isRoot)

  def leafRecords: Iterable[NodeRecord] = nodeRecordMap.values.filter(_.isLeaf)

  def rootRecord: NodeRecord = {
    require(isTree)
    rootRecords.head
  }
  // Also not circular!
  def isTree: Boolean = rootRecords.size == 1 && !nodeRecordMap.values.exists(_.isMultiParented)

  def newRootNode(): NodeType = ???

  def newRootEdge(): EdgeType = ???

  def mkTree(): Unit = {
    val visitor = new LinearGraphVisitor()

    // Remove edges that result in any node having multiple parents.
    visitor.foreachNodeRecord { nodeRecord =>
      if (nodeRecord.incoming.size > 1) {
        nodeRecord.incoming.tail.foreach { edgeRecord =>
          // Remove the outgoing edge from the parent
          edgeRecord.sourceRecord.subOutgoing(edgeRecord)
          // Remove the outgoing edge from the network
          edgeRecordMap -= edgeRecord.edge.getId
        }
        // Remove all but first incoming edges from the child.
        nodeRecord.incoming = Seq(nodeRecord.incoming.head)
      }
    }

    val oldRootRecords = rootRecords
    val rootNodeRecord = newNodeRecord(newRootNode())

    oldRootRecords.foreach { oldRootRecord =>
      newEdge(rootNodeRecord, newRootEdge(), oldRootRecord)
    }
  }
}