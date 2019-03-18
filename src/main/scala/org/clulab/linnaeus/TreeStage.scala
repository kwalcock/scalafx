package org.clulab.linnaeus

import javafx.stage.WindowEvent
import scalafx.application.JFXApp
import scalafx.application.JFXApp.PrimaryStage
import scalafx.scene.Scene
import scalafx.scene.control.Button
import scalafx.scene.layout.ColumnConstraints
import scalafx.scene.layout.GridPane
import scalafx.scene.layout.RowConstraints
import scalafx.stage.Stage

class TreeStage(val stageManager: StageManager) extends Stage {
  title = "Linnaeus Tree"
  scene = new Scene
  show
  stageManager.treeStage = Some(this)

  onCloseRequest = (windowEvent: WindowEvent) => {
    stageManager.closeTreeStage(windowEvent)
  }
}
