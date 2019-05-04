package org.clulab.linnaeus.stage.graphStage

import edu.uci.ics.jung.algorithms.layout.FRLayout
import edu.uci.ics.jung.algorithms.layout.SpringLayout
import edu.uci.ics.jung.algorithms.layout.TreeLayout
import edu.uci.ics.jung.graph.Graph
import edu.uci.ics.jung.graph.SparseMultigraph
import edu.uci.ics.jung.graph.UndirectedSparseGraph
import edu.uci.ics.jung.samples.SimpleGraphDraw
import edu.uci.ics.jung.visualization.VisualizationViewer
import edu.uci.ics.jung.visualization.decorators.ToStringLabeller
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

  protected def getGraphFromScratch(): Graph[_, _] = SimpleGraphDraw.getGraph()

  protected def getGraphFromEidos(): SparseMultigraph[EidosNode, String] = {
    val network = new EidosReader(ONTOLOGY_PATH).read()
    val rootRecord = network.rootRecord
    val graph = new SparseMultigraph[EidosNode, String]()

    def addChildren(parentRecord: network.NodeRecord, remaining: Int): Unit = {
      if (remaining > 0)
        parentRecord.outgoing.map(_.targetRecord).foreach { childRecord =>
          graph.addVertex(childRecord.node)
          graph.addEdge(parentRecord.node.name + " - " + childRecord.node.name, parentRecord.node, childRecord.node)
          addChildren(childRecord, remaining - 1)
        }
    }

    graph.addVertex(rootRecord.node)
    addChildren(rootRecord, 100)
    graph
  }

  title = "Linnaeus JUNG Graph"
  scene = new Scene(WIDTH, HEIGHT) {
//    val graph = getGraphFromScratch()
    val graph = getGraphFromEidos()
//    val layout = new FRLayout(graph)
    val layout = new SpringLayout(graph)
    val vv = new VisualizationViewer(layout)
    vv.getRenderContext.setVertexLabelTransformer(new ToStringLabeller)
    val swingNode = new SwingNode()
    swingNode.setContent(vv)

    val borderPane = new BorderPane() {
      center = swingNode
    }
    root = borderPane
  }
  show
}
