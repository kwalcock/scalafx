package org.clulab.linnaeus.graphStage

import com.google.common.base.Supplier
import edu.uci.ics.jung.algorithms.layout.FRLayout
import edu.uci.ics.jung.graph.UndirectedSparseGraph
import edu.uci.ics.jung.io.PajekNetReader
import edu.uci.ics.jung.samples.SimpleGraphDraw
import edu.uci.ics.jung.visualization.VisualizationViewer
import org.clulab.linnaeus.StageManager
import scalafx.embed.swing.SwingNode
import scalafx.scene.Scene
import scalafx.scene.layout.BorderPane

class JUNGStage(stageManager: StageManager) extends GraphStage(stageManager) {
  val WIDTH = 400
  val HEIGHT = 400

  title = "Linnaeus JUNG Graph"
  scene = new Scene(WIDTH, HEIGHT) {
    val graph = SimpleGraphDraw.getGraph()
    val vv = new VisualizationViewer(new FRLayout(graph))
    val swingNode = new SwingNode()
    swingNode.setContent(vv)

    val borderPane = new BorderPane() {
      center = swingNode
    }
    root = borderPane
  }
  show
}
