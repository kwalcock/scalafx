package org.clulab.linnaeus.stage

import scalafx.Includes._
import scalafx.application.JFXApp.PrimaryStage
import scalafx.event.ActionEvent
import scalafx.scene.Scene
import scalafx.scene.control.Button
import scalafx.scene.control.CheckMenuItem
import scalafx.scene.control.Menu
import scalafx.scene.control.MenuBar
import scalafx.scene.control.MenuItem
import scalafx.scene.control.SeparatorMenuItem
import scalafx.scene.layout.BorderPane
import scalafx.scene.layout.ColumnConstraints
import scalafx.scene.layout.GridPane
import scalafx.scene.layout.RowConstraints
import scalafx.stage.WindowEvent

class MainStage(val stageManager: StageManager) extends PrimaryStage {
  title = "Linnaeus"
  scene = new Scene(300, 300) {
    val menuBar = new MenuBar {
      menus = List(
        new Menu("_File") {
          mnemonicParsing = true
          items = List(
            new MenuItem("_New..."),
            new MenuItem("_Open..."),
            new MenuItem("Save"),
            new MenuItem("Save _As..."),
            new SeparatorMenuItem,
            new MenuItem("E_xit")
          )
        },
        new Menu("_View") {
          mnemonicParsing = true
          items = List(
            new CheckMenuItem("Table") { selected = true },
            new CheckMenuItem("Tree") { selected = true },
            new CheckMenuItem("Text") { selected = true }
          )
        },
        new Menu("_Help") {
          mnemonicParsing = true
          items = List(
            new MenuItem("_About Linnaeus...")
          )
        }
      )
    }
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

    val borderPane = new BorderPane() {
      top = menuBar
      center = gridPane
    }

    root = borderPane
  }

  stageManager.mainStageOpt = Some(this)

  onCloseRequest = (windowEvent: WindowEvent) => {
    stageManager.closeMainStage(windowEvent)
  }
}
