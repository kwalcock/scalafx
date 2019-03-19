package org.clulab.linnaeus

import scalafx.Includes._

import scalafx.scene.Scene
import scalafx.scene.control.TreeItem
import scalafx.scene.control.TreeView
import scalafx.stage.Stage
import scalafx.stage.WindowEvent

import org.clulab.linnaeus.model.TreeNode

class TreeStage(val stageManager: StageManager) extends Stage {
  protected val LEVELS = 10
  protected val COUNT = 100000

  title = "Linnaeus Tree"
  scene = new Scene(400, 400) {
    val treeNode: TreeItem[String] = TreeNode.random(LEVELS, COUNT)
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
