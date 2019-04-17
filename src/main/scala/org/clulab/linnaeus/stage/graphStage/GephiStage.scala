package org.clulab.linnaeus.stage.graphStage

//import org.gephi.visualization.api.

import org.clulab.linnaeus.stage.StageManager
import scalafx.scene.Scene

class GephiStage(stageManager: StageManager) extends GraphStage(stageManager) {

  title = "Linnaeus Gephi Graph"
  scene = new Scene(400, 400) {

  }
  show
}
