package org.clulab.linnaeus.stage.graphStage

import org.clulab.linnaeus.stage.StageManager
import scalafx.Includes._
import scalafx.stage.Stage
import scalafx.stage.WindowEvent

class GraphStage(val stageManager: StageManager) extends Stage {
  stageManager.graphStageOpt = Some(this)

  onCloseRequest = { windowEvent: WindowEvent =>
    stageManager.closeGraphStage(windowEvent)
  }
}
