package org.clulab.linnaeus.stage.graphStage

import info.gianlucacosta.eighthbridge.fx.canvas.GraphCanvas
import info.gianlucacosta.eighthbridge.fx.canvas.basic.DefaultBasicLink
import info.gianlucacosta.eighthbridge.fx.canvas.basic.DefaultBasicVertex
import info.gianlucacosta.eighthbridge.fx.canvas.basic.ReadOnlyController
import info.gianlucacosta.eighthbridge.graphs.Binding
import info.gianlucacosta.eighthbridge.graphs.Graph
import info.gianlucacosta.eighthbridge.graphs.Link
import info.gianlucacosta.eighthbridge.graphs.Vertex
import info.gianlucacosta.eighthbridge.graphs.point2point.ArcBinding
import info.gianlucacosta.eighthbridge.graphs.point2point.EdgeBinding
import info.gianlucacosta.eighthbridge.graphs.point2point.visual.DefaultVisualGraph
import info.gianlucacosta.eighthbridge.graphs.point2point.visual.DefaultVisualLink
import info.gianlucacosta.eighthbridge.graphs.point2point.visual.DefaultVisualVertex
import java.util.UUID

import org.clulab.linnaeus.stage.StageManager
import scalafx.Includes._
import scalafx.geometry.Point2D
import scalafx.scene.Scene
import scalafx.stage.Stage
import scalafx.stage.WindowEvent

/**
  * This isn't working and a personal message to the author was not answered.
  */
class EighthBridgeGraph(val vertexes: Set[Vertex], val links: Set[Link], val bindings: Set[Binding]) extends Graph[Vertex, Link, Binding, EighthBridgeGraph] {

  override protected def graphCopy(vertexes: Set[Vertex], links: Set[Link], bindings: Set[Binding]): EighthBridgeGraph =
      new EighthBridgeGraph(vertexes, links, bindings)
}


class EighthBridgeStage(stageManager: StageManager) extends GraphStage(stageManager) {
  protected val LEVELS = 10
  protected val COUNT = 100000

  protected def mkGraph(): EighthBridgeGraph = {
    val v1: Vertex = new DefaultBasicVertex("V1")
    val v2: Vertex = new DefaultBasicVertex("V2")
    val vs = Set(v1, v2)
    val l1: Link = new DefaultBasicLink("L1")
    val ls = Set(l1)
    val b1: Binding = new EdgeBinding(UUID.randomUUID, vs.map(_.id), l1.id)
    val bs = Set(b1)
    val g1 = new EighthBridgeGraph(vs, ls, bs)

    g1
  }

  protected def mkVisualGraph(): DefaultVisualGraph[DefaultVisualVertex, DefaultVisualLink] = {
    val v1: DefaultVisualVertex = new DefaultVisualVertex(new Point2D(0d, 0d))
    val v2: DefaultVisualVertex = new DefaultVisualVertex(new Point2D(1d, 1d))
    val vs = Set(v1, v2)
    val l1: DefaultVisualLink = new DefaultVisualLink()
    val ls = Set(l1)
    val b1: ArcBinding = new ArcBinding(UUID.randomUUID, v1.id, v2.id, l1.id)
    val bs = Set(b1)
    val g1 = new DefaultVisualGraph(vs, ls, bs)

    g1
  }

  title = "Linnaeus EighthBridge Graph"
  scene = new Scene(400, 400) {
    val controller = new ReadOnlyController(renderDirected = false)
    val visualGraph = mkVisualGraph
//    val graphCanvas = new GraphCanvas(controller, visualGraph)

//    root = new JPane
  }

//  scene.stylesheets.addAll (
//    BasicStyles.resourceUrl.toExternalForm
//  )

  show
}
