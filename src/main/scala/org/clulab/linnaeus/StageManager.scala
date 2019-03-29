package org.clulab.linnaeus

import org.clulab.linnaeus.graphStage.GraphStage
import scalafx.application.Platform
import scalafx.event.ActionEvent
import scalafx.scene.control.TreeItem
import scalafx.stage.WindowEvent
import org.clulab.linnaeus.model.TableNode

class StageManager {
  var mainStageOpt: Option[MainStage] = None
  var tableStageOpt: Option[TableStage] = None
  var treeStageOpt: Option[TreeStage] = None
  var textStageOpt: Option[TextStage] = None
  var graphStageOpt: Option[GraphStage] = None

  def closeMainStage(windowEvent: WindowEvent): Unit = {
    windowEvent.consume()
    Platform.exit()
  }

  def closeTableStage(event: WindowEvent): Unit = {
    event.consume()
    tableStageOpt.get.hide()
  }

  def openTableStage(event: ActionEvent): Unit = {
    event.consume()
    tableStageOpt.get.show()
  }

  def closeTreeStage(event: WindowEvent): Unit = {
    event.consume()
    treeStageOpt.get.hide()
  }

  def openTreeStage(event: ActionEvent): Unit = {
    event.consume()
    treeStageOpt.get.show()
  }

  def closeTextStage(event: WindowEvent): Unit = {
    event.consume()
    textStageOpt.get.hide()
  }

  def openTextStage(event: ActionEvent): Unit = {
    // Get position and other state info first
    event.consume()
    textStageOpt.get.show()
  }

  def changedTableSelection(relationOpt: Option[TableNode]): Unit = {
    textStageOpt.foreach(_.changedTableSelection(relationOpt))
  }

  def changedTreeSelection(treeItemOpt: Option[TreeItem[String]]): Unit = {
    textStageOpt.foreach(_.changedTreeSelection(treeItemOpt))
  }

  def openGraphStage(event: ActionEvent): Unit = {
    event.consume()
    graphStageOpt.get.show()
  }

  def closeGraphStage(event: WindowEvent): Unit = {
    event.consume()
    graphStageOpt.get.hide()
  }
}
