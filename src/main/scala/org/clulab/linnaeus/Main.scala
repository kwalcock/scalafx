package org.clulab.linnaeus

import scalafx.application.JFXApp
import scalafx.application.JFXApp.PrimaryStage
import scalafx.application.Platform
import scalafx.scene.Scene
import scalafx.scene.control.Button
import scalafx.scene.layout.ColumnConstraints
import scalafx.scene.layout.GridPane
import scalafx.scene.layout.RowConstraints
import scalafx.scene.layout.VBox
import scalafx.scene.text.Text

object Main extends JFXApp {
  Platform.implicitExit = false
  val stageManager = new StageManager

  val mainStage = new MainStage(stageManager)
  val tableStage = new TableStage(stageManager)
  val treeStage = new TreeStage(stageManager)
  val textStage = new TextStage(stageManager)
}
