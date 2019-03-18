package org.clulab.linnaeus

import javafx.stage.WindowEvent
import scalafx.application.Platform
import scalafx.scene.Scene
import scalafx.stage.Stage

class TableStage(val stageManager: StageManager) extends Stage {
  title = "Linnaeus Table"
  scene = new Scene
  show
  stageManager.tableStage = Some(this)

  onCloseRequest = (windowEvent: WindowEvent) => {
    stageManager.closeTableStage(windowEvent)
  }
}
