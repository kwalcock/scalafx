package org.clulab.linnaeus.graphStage

import org.clulab.linnaeus.StageManager
import org.graphstream.ui.graphicGraph.GraphicGraph
import org.graphstream.ui.swingViewer.DefaultView
import org.graphstream.ui.swingViewer.basicRenderer.SwingBasicGraphRenderer
import org.graphstream.ui.view.Viewer
import scalafx.Includes._
import scalafx.embed.swing.SwingNode
import scalafx.scene.Scene
import scalafx.scene.layout.BorderPane

class GraphStreamStage(stageManager: StageManager) extends GraphStage(stageManager) {

  title = "Linnaeus GraphStreasm Graph"
  scene = new Scene(400, 400) {
    val graph = new GraphicGraph("Tutorial 1")

    graph.addNode("A")
    graph.addNode("B")
    graph.addNode("C")
    graph.addEdge("AB", "A", "B")
    graph.addEdge("BC", "B", "C")
    graph.addEdge("CA", "C", "A")

    val viewer = new Viewer(graph)
    val renderer = new SwingBasicGraphRenderer()
    val view = new DefaultView(viewer,"testing", renderer)

    val swingNode: SwingNode = new SwingNode()
    swingNode.setContent(view)

    val borderPane = new BorderPane() {
      center = swingNode
    }
    root = borderPane

    view.display(graph, false)
  }
  show
}
