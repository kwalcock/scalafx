package org.clulab.linnaeus

import javafx.stage.WindowEvent
import scalafx.application.JFXApp
import scalafx.application.JFXApp.PrimaryStage
import scalafx.application.Platform
import scalafx.event.ActionEvent
import scalafx.scene.Scene
import scalafx.scene.control.Button
import scalafx.scene.layout.ColumnConstraints
import scalafx.scene.layout.GridPane
import scalafx.scene.layout.RowConstraints

class StageManager {
  var mainStage: Option[MainStage] = None
  var tableStage: Option[TableStage] = None
  var treeStage: Option[TreeStage] = None
  var textStage: Option[TextStage] = None

  def closeMainStage(windowEvent: WindowEvent): Unit = {
    windowEvent.consume()
    Platform.exit()
  }

  def closeTableStage(event: WindowEvent): Unit = {
    event.consume()
    tableStage.get.hide()
  }

  def openTableStage(event: ActionEvent): Unit = {
    event.consume()
    tableStage.get.show()
  }

  def closeTreeStage(event: WindowEvent): Unit = {
    event.consume()
    treeStage.get.hide()
  }

  def openTreeStage(event: ActionEvent): Unit = {
    event.consume()
    treeStage.get.show()
  }

  def closeTextStage(event: WindowEvent): Unit = {
    event.consume()
    textStage.get.hide()
  }

  def openTextStage(event: ActionEvent): Unit = {
    // Get position and other state info first
    event.consume()
    textStage.get.show()
  }
}
