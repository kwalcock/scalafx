package org.clulab.linnaeus.graphStage

import edu.uci.ics.jung.algorithms.layout.FRLayout
import edu.uci.ics.jung.algorithms.layout.SpringLayout
import edu.uci.ics.jung.algorithms.layout.TreeLayout
import edu.uci.ics.jung.graph.Graph
import edu.uci.ics.jung.graph.SparseMultigraph
import edu.uci.ics.jung.graph.UndirectedSparseGraph
import edu.uci.ics.jung.samples.SimpleGraphDraw
import edu.uci.ics.jung.visualization.VisualizationViewer
import edu.uci.ics.jung.visualization.decorators.ToStringLabeller

import org.clulab.linnaeus.StageManager
import org.clulab.linnaeus.model.reader.EidosReader
import org.clulab.linnaeus.model.OntologyTreeItem

import scalafx.embed.swing.SwingNode
import scalafx.scene.Scene
import scalafx.scene.layout.BorderPane

class JUNGStage(stageManager: StageManager) extends GraphStage(stageManager) {
  val WIDTH = 800
  val HEIGHT = 800

  protected def getGraphFromScratch(): Graph[_, _] = SimpleGraphDraw.getGraph()

  protected def getGraphFromEidos(): SparseMultigraph[OntologyTreeItem, String] = {
    val ontologyRootItem = EidosReader.read()
    val graph = new SparseMultigraph[OntologyTreeItem, String]()

    def addChildren(ontologyTreeItem: OntologyTreeItem, remaining: Int): Unit = {
      if (remaining > 0)
        ontologyTreeItem.children.foreach { child =>
          graph.addVertex(child)
          graph.addEdge(ontologyTreeItem.toString + " - " + child.toString(), ontologyTreeItem, child)
          addChildren(child, remaining - 1)
        }
    }

    graph.addVertex(ontologyRootItem)
    addChildren(ontologyRootItem, 100)
    graph
  }

  title = "Linnaeus JUNG Graph"
  scene = new Scene(WIDTH, HEIGHT) {
//    val graph = getGraphFromScratch()
    val graph = getGraphFromEidos()
//    val layout = new FRLayout(graph)
    val layout = new SpringLayout(graph)
    val vv = new VisualizationViewer(layout)
    vv.getRenderContext.setVertexLabelTransformer(new ToStringLabeller)
    val swingNode = new SwingNode()
    swingNode.setContent(vv)

    val borderPane = new BorderPane() {
      center = swingNode
    }
    root = borderPane
  }
  show
}
