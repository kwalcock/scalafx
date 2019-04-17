package org.clulab.linnaeus.graphStage

import org.abego.treelayout.Configuration.Location

import org.clulab.linnaeus.StageManager

import com.fxgraph.cells.RectangleCell
import com.fxgraph.cells.TriangleCell
import com.fxgraph.edges.CorneredEdge
import com.fxgraph.edges.DoubleCorneredEdge
import com.fxgraph.edges.Edge
import com.fxgraph.graph.Graph
import com.fxgraph.graph.ICell
import com.fxgraph.graph.Model
import com.fxgraph.layout.AbegoTreeLayout

import scalafx.Includes._
import scalafx.geometry.Orientation
import scalafx.scene.Scene
import scalafx.scene.control.ScrollPane
import scalafx.scene.layout.BorderPane


class FXGraphStage(stageManager: StageManager) extends GraphStage(stageManager) {
  title = "Linnaeus FXGraph Graph"
  scene = new Scene(400, 400) {
    val tree: Graph = new Graph()
    addTreeComponents(tree)
    stylesheets.add("/application.css")

    val scrollPane = new ScrollPane()
    scrollPane.content = tree.getCanvas()

    val borderPane = new BorderPane() {
      center = scrollPane
    }
    root = borderPane
  }
  show

  protected def addTreeComponents(graph: Graph) = {
    val model: Model = graph.getModel()
    graph.beginUpdate()

    val cellA: ICell = new RectangleCell()
    val cellB: ICell = new RectangleCell()
    val cellC: ICell = new RectangleCell()
    val cellD: ICell = new TriangleCell()
    val cellE: ICell = new TriangleCell()
    val cellF: ICell = new RectangleCell()
    val cellG: ICell = new RectangleCell()

    model.addCell(cellA)
    model.addCell(cellB)
    model.addCell(cellC)
    model.addCell(cellD)
    model.addCell(cellE)
    model.addCell(cellF)
    model.addCell(cellG)

    val edgeAB: Edge = new Edge(cellA, cellB)
    edgeAB.textProperty().set("Edges can have text too!")
    model.addEdge(edgeAB)

    val  edgeAC:CorneredEdge = new CorneredEdge(cellA, cellC, Orientation.HORIZONTAL)
    edgeAC.textProperty().set("Edges can have corners too!")
    model.addEdge(edgeAC)
    model.addEdge(cellB, cellD)

    val edgeBE: DoubleCorneredEdge = new DoubleCorneredEdge(cellB, cellE, Orientation.HORIZONTAL)
    edgeBE.textProperty().set("You can implement custom edges and nodes too!")
    model.addEdge(edgeBE)
    model.addEdge(cellC, cellF)
    model.addEdge(cellC, cellG)

    graph.endUpdate()
    graph.layout(new AbegoTreeLayout(200, 200, Location.Top))
  }
}
