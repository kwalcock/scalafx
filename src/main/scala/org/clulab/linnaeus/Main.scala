package org.clulab.linnaeus

import org.clulab.linnaeus.graphStage.CytoscapeStage
import org.clulab.linnaeus.graphStage.EighthBridgeStage
import org.clulab.linnaeus.graphStage.FXGraphStage
import org.clulab.linnaeus.graphStage.GephiStage
import org.clulab.linnaeus.graphStage.GraphStage
import org.clulab.linnaeus.graphStage.GraphStreamStage
import org.clulab.linnaeus.graphStage.GraphVizStage
import org.clulab.linnaeus.graphStage.JGraphTStage
import org.clulab.linnaeus.graphStage.JGraphXStage
import org.clulab.linnaeus.graphStage.JUNGStage

import scalafx.application.JFXApp
import scalafx.application.Platform

object Main extends JFXApp {
  Platform.implicitExit = false
  val stageManager = new StageManager

  val mainStage = new MainStage(stageManager)
  val tableStage = new TableStage(stageManager)
  val treeStage = new TreeStage(stageManager)
  val textStage = new TextStage(stageManager)

//  val graphStage: GraphStage = new EighthBridgeStage(stageManager)
//  val graphStage: GraphStage = new GephiStage(stageManager)
//  val jGraphXStage:f GraphStage = new JGraphXStage(stageManager)
//  val cytoscapeStage: GraphStage = new CytoscapeStage(stageManager)
//  val fxGraph: GraphStage = new FXGraphStage(stageManager)
//  val graphStreamStage: GraphStage = new GraphStreamStage(stageManager)
//  val jGraphTStage: GraphStage = new JGraphTStage(stageManager)
//  val jungStage: GraphStage = new JUNGStage(stageManager)
  val graphVizStage: GraphStage = new GraphVizStage(stageManager)
}
