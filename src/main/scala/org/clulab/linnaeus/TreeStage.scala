package org.clulab.linnaeus

import scalafx.Includes._

import scalafx.scene.Scene
import scalafx.scene.control.TreeView
import scalafx.stage.Stage
import scalafx.stage.WindowEvent

import org.clulab.linnaeus.model.TreeNode

class TreeStage(val stageManager: StageManager) extends Stage {
  title = "Linnaeus Tree"
  scene = new Scene(400, 400) {
    val treeNode = TreeNode.random
    val treeView = new TreeView(treeNode) {
      selectionModel().selectedItem.onChange { (_, _, newValue) =>
        stageManager.changedTreeSelection(Option(newValue))
      }
    }

    root = treeView
  }
  stageManager.treeStageOpt = Some(this)
  show

  onCloseRequest = { windowEvent: WindowEvent =>
    stageManager.closeTreeStage(windowEvent)
  }
}
