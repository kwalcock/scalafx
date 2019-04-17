package org.clulab.linnaeus.stage.graphStage

import java.util.UUID

import info.gianlucacosta.eighthbridge.fx.canvas.basic.DefaultBasicLink
import info.gianlucacosta.eighthbridge.fx.canvas.basic.DefaultBasicVertex
import info.gianlucacosta.eighthbridge.fx.canvas.basic.ReadOnlyController
import info.gianlucacosta.eighthbridge.graphs.Binding
import info.gianlucacosta.eighthbridge.graphs.Link
import info.gianlucacosta.eighthbridge.graphs.Vertex
import info.gianlucacosta.eighthbridge.graphs.point2point.EdgeBinding
import info.gianlucacosta.eighthbridge.graphs.point2point.visual.DefaultVisualGraph
import info.gianlucacosta.eighthbridge.graphs.point2point.visual.DefaultVisualLink
import info.gianlucacosta.eighthbridge.graphs.point2point.visual.DefaultVisualVertex
import org.clulab.linnaeus.stage.StageManager
import scalafx.scene.Scene

class CytoscapeStage(stageManager: StageManager) extends GraphStage(stageManager) {
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
//    val v1: DefaultVisualVertex = new DefaultVisualVertex(new Point2D(0d, 0d))
//    val v2: DefaultVisualVertex = new DefaultVisualVertex(new Point2D(1d, 1d))
//    val vs = Set(v1, v2)
//    val l1: DefaultVisualLink = new DefaultVisualLink()
//    val ls = Set(l1)
//    val b1: ArcBinding = new ArcBinding(UUID.randomUUID, v1.id, v2.id, l1.id)
//    val bs = Set(b1)
//    val g1 = new DefaultVisualGraph(vs, ls, bs)
//
//    g1
    null
  }

  title = "Linnaeus EighthBridge Graph"
  scene = new Scene(400, 400) {
    val controller = new ReadOnlyController(renderDirected = false)
//    val visualGraph = mkVisualGraph
//    val graphCanvas = new GraphCanvas(controller, visualGraph)

//    root = new JPane
  }

//  scene.stylesheets.addAll (
//    BasicStyles.resourceUrl.toExternalForm
//  )

  show
}
