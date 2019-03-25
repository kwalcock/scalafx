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
//    val rootTreeItem: TreeItem[String] = TreeNode.random(LEVELS, COUNT)
    val rootTreeItem: TreeItem[String] = TreeNode.fromEidos

    val treeView = new TreeView(rootTreeItem) {
      selectionModel().selectedItem.onChange { (_, _, newValue) =>
        stageManager.changedTreeSelection(Option(newValue))
      }
    }
    treeView.showRoot = false

    root = treeView
  }
  stageManager.treeStageOpt = Some(this)
  show

  onCloseRequest = { windowEvent: WindowEvent =>
    stageManager.closeTreeStage(windowEvent)
  }
}
