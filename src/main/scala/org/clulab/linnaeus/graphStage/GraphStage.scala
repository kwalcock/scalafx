package org.clulab.linnaeus.graphStage

import scalafx.Includes._
import scalafx.stage.Stage
import scalafx.stage.WindowEvent

import org.clulab.linnaeus.StageManager

class GraphStage(val stageManager: StageManager) extends Stage {
  stageManager.graphStageOpt = Some(this)

  onCloseRequest = { windowEvent: WindowEvent =>
    stageManager.closeGraphStage(windowEvent)
  }
}
