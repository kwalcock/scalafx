package org.clulab.linnaeus.graphStage

import com.mxgraph.swing.mxGraphComponent
import com.mxgraph.view.mxGraph

import org.clulab.linnaeus.StageManager

import com.mxgraph.layout.mxCircleLayout
import com.mxgraph.swing.mxGraphComponent

import org.jgrapht.ListenableGraph
import org.jgrapht.ext.JGraphXAdapter
import org.jgrapht.graph.DefaultDirectedGraph
import org.jgrapht.graph.DefaultEdge
import org.jgrapht.graph.DefaultListenableGraph

import scalafx.Includes._
import scalafx.embed.swing.SwingNode
import scalafx.scene.Scene
import scalafx.scene.layout.BorderPane

class JGraphTStage(stageManager: StageManager) extends GraphStage(stageManager) {
  val WIDTH = 400
  val HEIGHT = 400


  protected def populateGraph(graph: DefaultListenableGraph[String, DefaultEdge]): Unit = {
    val v1 = "v1"
    val v2 = "v2"
    val v3 = "v3"
    val v4 = "v4"

    graph.addVertex(v1)
    graph.addVertex(v2)
    graph.addVertex(v3)
    graph.addVertex(v4)

    graph.addEdge(v1, v2)
    graph.addEdge(v2, v3)
    graph.addEdge(v3, v1)
    graph.addEdge(v4, v3)
  }

  protected def mkLayout(jgxAdapter: JGraphXAdapter[String, DefaultEdge]): mxCircleLayout = {
    val layout = new mxCircleLayout(jgxAdapter)
    val radius = 100

    layout.setX0((WIDTH / 2.0) - radius)
    layout.setY0((HEIGHT / 2.0) - radius)
    layout.setRadius(radius)
    layout.setMoveCircle(true)
    layout
  }

  title = "Linnaeus JGraphT Graph"
  scene = new Scene(WIDTH, HEIGHT) {
    val graph = new DefaultListenableGraph[String, DefaultEdge](new DefaultDirectedGraph[String, DefaultEdge](classOf[DefaultEdge]))
    populateGraph(graph)

    val jgxAdapter = new JGraphXAdapter[String, DefaultEdge](graph)
    val swingNode: SwingNode = new SwingNode()
    val component = new mxGraphComponent(jgxAdapter)
    component.setConnectable(false)
    component.getGraph.setAllowDanglingEdges(false)
    swingNode.setContent(component)

    val layout = mkLayout(jgxAdapter)
    layout.execute(jgxAdapter.getDefaultParent)

    val borderPane = new BorderPane() {
      center = swingNode
    }
    root = borderPane
  }
  show
}
