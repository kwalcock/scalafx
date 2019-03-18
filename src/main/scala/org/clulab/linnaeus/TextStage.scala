package org.clulab.linnaeus

import javafx.stage.WindowEvent
import scalafx.scene.Scene
import scalafx.stage.Stage

class TextStage(val stageManager: StageManager) extends Stage {
  title = "Linnaeus Text"
  scene = new Scene
  show
  stageManager.textStage = Some(this)

  onCloseRequest = (windowEvent: WindowEvent) => {
    stageManager.closeTextStage(windowEvent)
  }
}
