package org.clulab.linnaeus.graphStage

import com.mxgraph.swing.mxGraphComponent
import com.mxgraph.view.mxGraph

import org.clulab.linnaeus.StageManager

import scalafx.Includes._
import scalafx.embed.swing.SwingNode
import scalafx.scene.Scene
import scalafx.scene.layout.BorderPane

class JGraphXStage(stageManager: StageManager) extends GraphStage(stageManager) {
  title = "Linnaeus JGraphX Graph"
  scene = new Scene(400, 400) {

    val graph = new mxGraph
    val parent: Any = graph.getDefaultParent

    graph.getModel.beginUpdate()
    try {
      val v1 = graph.insertVertex(parent, null, "Hello", 20, 20, 80, 30)
      val v2 = graph.insertVertex(parent, null, "World!", 240, 150, 80, 30)
      graph.insertEdge(parent, null, "Edge", v1, v2)
    }
    finally graph.getModel.endUpdate()

    val graphComponent: mxGraphComponent = new mxGraphComponent(graph)
    val swingNode: SwingNode = new SwingNode()
    swingNode.setContent(graphComponent)

    val borderPane = new BorderPane() {
      center = swingNode
    }
    root = borderPane
  }
  show
}
