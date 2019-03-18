package org.clulab.linnaeus

import java.awt.BorderLayout

import javafx.event.EventHandler
import javafx.stage.WindowEvent
import org.clulab.linnaeus.Main.stage
import scalafx.application.JFXApp.PrimaryStage
import scalafx.application.Platform
import scalafx.event.ActionEvent
import scalafx.scene.Scene
import scalafx.scene.control.Button
import scalafx.scene.layout.ColumnConstraints
import scalafx.scene.layout.GridPane
import scalafx.scene.layout.RowConstraints
import scalafx.stage.Stage
import scalafx.Includes._
import scalafx.scene.layout.BorderPane
import scalafx.scene.layout.Priority
import scalafx.scene.layout.Region
import scalafx.scene.layout.VBox
import scalafx.scene.paint.Color.{Black, Red, White}

class MainStage(val stageManager: StageManager) extends PrimaryStage {
  title = "Linnaeus"
//  resizable = false
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

    val gridPane = new GridPane {
//      gridLinesVisible = true
      fill = White
    }
    gridPane.add(table, 0, 0)
    gridPane.add(tree, 0, 1)
    gridPane.add(text, 0, 2)

    val columnConstraints = new ColumnConstraints()
    columnConstraints.setPercentWidth(100)
//    columnConstraints.setHgrow(Priority.Always)
    columnConstraints.setFillWidth(true)
    gridPane.getColumnConstraints.add(columnConstraints)



    val tableRowConstraints = new RowConstraints()
    tableRowConstraints.setPercentHeight(33.333)
    tableRowConstraints.setFillHeight(true)

    val treeRowConstraints = new RowConstraints()
    treeRowConstraints.setPercentHeight(33.333)
    val textRowConstraints = new RowConstraints()
    textRowConstraints.setPercentHeight(33.333)
    gridPane.getRowConstraints.addAll(tableRowConstraints, treeRowConstraints, textRowConstraints)

    gridPane.setPrefSize(300, 300)
    gridPane.setMaxSize(Double.MaxValue, Double.MaxValue) // Region.USE_COMPUTED_SIZE, Region.USE_COMPUTED_SIZE)
    content = gridPane

//    val vBox = new VBox()
//    vBox.width = 400
//    vBox.fillWidth = true // fill = Red
//    vBox.maxWidth = Double.MaxValue
//    vBox.children.addAll(table, tree, text)
//    content = vBox
//
//    maxWidth = Double.MaxValue
//    fill = Black
  }

  stageManager.mainStage = Some(this)

  onCloseRequest = (windowEvent: WindowEvent) => {
    stageManager.closeMainStage(windowEvent)
  }
}
