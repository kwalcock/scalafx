package org.clulab.linnaeus

import scalafx.application.JFXApp
import scalafx.application.Platform

object Main extends JFXApp {
  Platform.implicitExit = false
  val stageManager = new StageManager

  val mainStage = new MainStage(stageManager)
  val tableStage = new TableStage(stageManager)
  val treeStage = new TreeStage(stageManager)
  val textStage = new TextStage(stageManager)
}
