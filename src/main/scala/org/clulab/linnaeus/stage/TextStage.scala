package org.clulab.linnaeus.stage

import org.clulab.linnaeus.model.TableNode
import scalafx.Includes._
import scalafx.scene.Scene
import scalafx.scene.control.TreeItem
import scalafx.scene.web.WebView
import scalafx.stage.Stage
import scalafx.stage.WindowEvent

class TextStage(val stageManager: StageManager) extends Stage {
  var currentRelationOpt: Option[TableNode] = None
  var currentPathOpt: Option[String] = None
  val webView = new WebView

  def changedTableSelection(relationOpt: Option[TableNode]): Unit = {
    currentRelationOpt = relationOpt
    showContent
  }

  def changedTreeSelection(treeItemOpt: Option[TreeItem[String]]): Unit = {
    def getAncestors(treeItem: TreeItem[String]): List[TreeItem[String]] = {
      Option(treeItem).map { treeItem =>
        val parent = treeItem.parent.get()

        treeItem :: getAncestors(parent)
      }.getOrElse(Nil)
    }

    currentPathOpt = treeItemOpt.map { treeItem =>
      val ancestors = getAncestors(treeItem).reverse

      ancestors.map(_.getValue()).mkString("/")
    }
    showContent
  }

  protected def showContent = {
    val tableContent =
        if (currentRelationOpt.isDefined) {
          "<b>table value:</b> " + currentRelationOpt.get.toString
        }
        else {
          "Nothing is selected in the table"
        }
    val treeContent =
      if (currentPathOpt.isDefined) {
        "<b>tree value:</b> " + currentPathOpt.get.toString
      }
      else {
        "Nothing is selected in the tree"
      }

    webView.engine.loadContent(tableContent + "<br>" + treeContent)
  }

  title = "Linnaeus Text"
  scene = new Scene(1000, 300) {
    webView.engine.loadContent("<b>Hello, world!</b>")
    root = webView
  }
  stageManager.textStageOpt = Some(this)
  show

  onCloseRequest = { windowEvent: WindowEvent =>
    stageManager.closeTextStage(windowEvent)
  }
}
