package org.clulab.linnaeus

import scalafx.Includes._

import scalafx.application.JFXApp.PrimaryStage
import scalafx.event.ActionEvent
import scalafx.scene.control.Button
import scalafx.scene.layout.ColumnConstraints
import scalafx.scene.layout.GridPane
import scalafx.scene.layout.RowConstraints
import scalafx.scene.Scene
import scalafx.stage.WindowEvent

class MainStage(val stageManager: StageManager) extends PrimaryStage {
  title = "Linnaeus"
  scene = new Scene(300, 300) {
    val table = new Button {
      text = "Table"
      onAction = (actionEvent: ActionEvent) => {
        stageManager.openTableStage(actionEvent)
      }
      maxWidth = Double.MaxValue
      maxHeight = Double.MaxValue
    }
    val tree = new Button {
      text = "Tree"
      onAction = (actionEvent: ActionEvent) => {
        stageManager.openTreeStage(actionEvent)
      }
      maxWidth = Double.MaxValue
      maxHeight = Double.MaxValue
    }
    val text = new Button {
      text = "Text"
      onAction = (actionEvent: ActionEvent) => {
        stageManager.openTextStage(actionEvent)
      }
      maxWidth = Double.MaxValue
      maxHeight = Double.MaxValue
    }

    val gridPane = new GridPane
    gridPane.add(table, 0, 0)
    gridPane.add(tree, 0, 1)
    gridPane.add(text, 0, 2)

    val columnConstraints = new ColumnConstraints()
    columnConstraints.setPercentWidth(100)
    gridPane.getColumnConstraints.add(columnConstraints)

    val tableRowConstraints = new RowConstraints()
    tableRowConstraints.setPercentHeight(33.333)
    val treeRowConstraints = new RowConstraints()
    treeRowConstraints.setPercentHeight(33.333)
    val textRowConstraints = new RowConstraints()
    textRowConstraints.setPercentHeight(33.333)
    gridPane.getRowConstraints.addAll(tableRowConstraints, treeRowConstraints, textRowConstraints)

    root = gridPane
  }

  stageManager.mainStageOpt = Some(this)

  onCloseRequest = (windowEvent: WindowEvent) => {
    stageManager.closeMainStage(windowEvent)
  }
}
