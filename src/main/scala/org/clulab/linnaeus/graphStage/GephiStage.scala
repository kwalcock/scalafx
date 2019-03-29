package org.clulab.linnaeus.graphStage

import org.clulab.linnaeus.StageManager

//import org.gephi.visualization.api.

import scalafx.scene.Scene

class GephiStage(stageManager: StageManager) extends GraphStage(stageManager) {

  title = "Linnaeus Gephi Graph"
  scene = new Scene(400, 400) {

  }
  show
}
