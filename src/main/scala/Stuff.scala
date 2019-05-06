class GraphNode[NodeIdentityType]

class GraphEdge[EdgeIdentityType]

class GraphNetwork[
  NetworkIdentityType,
  NodeIdentityType, NodeType <: GraphNode[NodeIdentityType],
  EdgeIdentityType, EdgeType <: GraphEdge[EdgeIdentityType]
] {

  class NodeRecord {
    var incoming: Seq[EdgeRecord] = Seq.empty
    var outgoing: Seq[EdgeRecord] = Seq.empty

    var valueOpt: Option[NodeType] = None
  }

  class EdgeRecord(val sourceRecord: NodeRecord, val edge: EdgeType, val targetRecord: NodeRecord) {
    var valueOpt: Option[EdgeType] = None
  }

  val root = new NodeRecord
}

class MyNode extends GraphNode[Int]

class MyEdge extends GraphEdge[Int]

class MyNetwork extends GraphNetwork[
  Int,
  Int, MyNode,
  Int, MyEdge
]

object NetworkTest {
  // Can this be expressed without "network"?
  def doStuff(nodeRecord: MyNetwork#NodeRecord): Unit = println("hello")
}

object MyNetworkTest extends App {

  def doStuff(nodeRecord: network.NodeRecord): Unit = println("hello")

  val network = new MyNetwork

  doStuff(network.root)

  NetworkTest.doStuff(network.root)
}
