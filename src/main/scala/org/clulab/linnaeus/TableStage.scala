package org.clulab.linnaeus

import scalafx.Includes._

import scalafx.beans.property.ObjectProperty
import scalafx.beans.property.StringProperty
import scalafx.collections.ObservableBuffer
import scalafx.scene.Scene
import scalafx.scene.control.TableColumn
import scalafx.scene.control.TableView
import scalafx.stage.Stage
import scalafx.stage.WindowEvent

import org.clulab.linnaeus.model.TableNode

class TableStage(val stageManager: StageManager) extends Stage {
  val COUNT = 100000

  title = "Linnaeus Table"
  scene = new Scene(900, 400) {
    val tableNodes = ObservableBuffer.empty[TableNode]
    0.until(COUNT).foreach(_ => tableNodes.append(TableNode.random))

    val tableView = new TableView(tableNodes) {
      selectionModel().selectedItem.onChange { (_, _, newValue) =>
         stageManager.changedTableSelection(Option(newValue))
      }

      val hypernymCol = new TableColumn[TableNode, String]("Hypernym")
      hypernymCol.cellValueFactory = cellDataFeature => StringProperty(cellDataFeature.value.hypernym)

      val hyponymCol = new TableColumn[TableNode, String]("Hyponym")
      hyponymCol.cellValueFactory = cellDataFeature => StringProperty(cellDataFeature.value.hyponym)

      val exampleCol = new TableColumn[TableNode, String]("Example")
      exampleCol.cellValueFactory = cellDataFeature => StringProperty(cellDataFeature.value.example)

      val priorityCol = new TableColumn[TableNode, Int]("Priority")
      priorityCol.cellValueFactory = cellDataFeature => ObjectProperty(cellDataFeature.value.priority)

      columns ++= List(hypernymCol, hyponymCol, exampleCol, priorityCol)
    }
    root = tableView
  }

  stageManager.tableStageOpt = Some(this)
  show

  onCloseRequest = { windowEvent: WindowEvent =>
    stageManager.closeTableStage(windowEvent)
  }
}
