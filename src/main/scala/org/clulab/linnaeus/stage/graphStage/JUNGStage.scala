package org.clulab.linnaeus.stage.graphStage

//import edu.uci.ics.jung.algorithms.layout.FRLayout
import edu.uci.ics.jung.algorithms.layout.SpringLayout
//import edu.uci.ics.jung.algorithms.layout.TreeLayout
import edu.uci.ics.jung.graph.Graph
import edu.uci.ics.jung.graph.SparseMultigraph
import edu.uci.ics.jung.samples.SimpleGraphDraw
import edu.uci.ics.jung.visualization.VisualizationViewer
import edu.uci.ics.jung.visualization.decorators.ToStringLabeller
import org.clulab.linnaeus.model.graph.eidos.EidosNetwork
import org.clulab.linnaeus.model.reader.EidosReader
import org.clulab.linnaeus.model.graph.eidos.EidosNode
import org.clulab.linnaeus.stage.StageManager
import scalafx.embed.swing.SwingNode
import scalafx.scene.Scene
import scalafx.scene.layout.BorderPane

class JUNGStage(stageManager: StageManager) extends GraphStage(stageManager) {
  protected val ONTOLOGY_PATH =  "data/un_ontology.yml"

  val WIDTH = 800
  val HEIGHT = 800

  protected def mkGraphFromScratch(): Graph[_, _] = SimpleGraphDraw.getGraph

  protected def mkGraphFromEidos(): SparseMultigraph[EidosNode, String] = {
    val network = new EidosReader(ONTOLOGY_PATH).read()
    val rootPacket = network.rootPacket
    val graph = new SparseMultigraph[EidosNode, String]()

    def addChildren(parentPacket: EidosNetwork#NodePacket, remaining: Int): Unit = {
      if (remaining > 0)
        parentPacket.outgoing.map(_.targetPacket).foreach { childPacket =>
          graph.addVertex(childPacket.node)
          graph.addEdge(parentPacket.node.name + " - " + childPacket.node.name, parentPacket.node, childPacket.node)
          addChildren(childPacket, remaining - 1)
        }
    }

    graph.addVertex(rootPacket.node)
    addChildren(rootPacket, 100)
    graph
  }

  title = "Linnaeus JUNG Graph"
  scene = new Scene(WIDTH, HEIGHT) {
//    val graph = getGraphFromScratch()
    val graph: SparseMultigraph[EidosNode, String] = mkGraphFromEidos()
//    val layout = new FRLayout(graph)
    val layout = new SpringLayout(graph)
    val vv = new VisualizationViewer(layout)
    vv.getRenderContext.setVertexLabelTransformer(new ToStringLabeller)
    val swingNode = new SwingNode()
    swingNode.setContent(vv)

    val borderPane: BorderPane = new BorderPane() {
      center = swingNode
    }
    root = borderPane
  }
  show
}
