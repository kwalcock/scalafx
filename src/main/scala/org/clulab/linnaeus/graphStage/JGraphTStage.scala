package org.clulab.linnaeus.graphStage

import com.mxgraph.swing.mxGraphComponent
import com.mxgraph.view.mxGraph
import org.clulab.linnaeus.StageManager
import com.mxgraph.layout.mxCircleLayout
import com.mxgraph.layout.mxCompactTreeLayout
import com.mxgraph.swing.mxGraphComponent
import org.clulab.linnaeus.model.OntologyTree
import org.clulab.linnaeus.model.OntologyTreeItem
import org.jgrapht.ListenableGraph
import org.jgrapht.ext.JGraphXAdapter
import org.jgrapht.graph.DefaultDirectedGraph
import org.jgrapht.graph.DefaultEdge
import org.jgrapht.graph.DefaultListenableGraph

import scalafx.Includes._
import scalafx.embed.swing.SwingNode
import scalafx.scene.Scene
import scalafx.scene.layout.BorderPane

import scala.annotation.tailrec
import scala.collection.mutable

class JGraphTStage(stageManager: StageManager) extends GraphStage(stageManager) {
  protected val ONTOLOGY_PATH =  "/org/clulab/wm/eidos/english/ontologies/un_ontology.yml"

  val WIDTH = 400
  val HEIGHT = 400

  protected def newGraphAdapterFromScratch(): JGraphXAdapter[String, DefaultEdge] = {
    val graph = new DefaultListenableGraph[String, DefaultEdge](new DefaultDirectedGraph[String, DefaultEdge](classOf[DefaultEdge]))
    val jgxAdapter = new JGraphXAdapter[String, DefaultEdge](graph)

    val v1 = "v1"
    val v2 = "v2"
    val v3 = "v3"
    val v4 = "v4"

    graph.addVertex(v1)
    graph.addVertex(v2)
    graph.addVertex(v3)
    graph.addVertex(v4)

    graph.addEdge(v1, v2)
    graph.addEdge(v2, v3)
    graph.addEdge(v3, v1)
    graph.addEdge(v4, v3)

    jgxAdapter
  }

  class MyEdge extends DefaultEdge {
    override def toString(): String = ""
  }

  protected def newGraphAdapterFromEidos(): JGraphXAdapter[OntologyTreeItem, MyEdge] = {
    val ontologyRootItem = OntologyTree.mkTree()
//    val graph = new DefaultListenableGraph[OntologyTreeItem, DefaultEdge](new DefaultDirectedGraph[OntologyTreeItem, DefaultEdge](classOf[DefaultEdge]))
    val graph = new DefaultListenableGraph[OntologyTreeItem, MyEdge](new DefaultDirectedGraph[OntologyTreeItem, MyEdge](classOf[MyEdge]))
    val jgxAdapter = new JGraphXAdapter[OntologyTreeItem, MyEdge](graph)

    def addChildren(ontologyTreeItem: OntologyTreeItem, remaining: Int): Unit = {
      if (remaining > 0)
        ontologyTreeItem.children.foreach { child =>
          graph.addVertex(child)
          graph.addEdge(ontologyTreeItem, child, new MyEdge())
          addChildren(child, remaining - 1)
        }
    }

    graph.addVertex(ontologyRootItem)
    addChildren(ontologyRootItem, 100)
    jgxAdapter
  }

  protected def mkLayout[T](jgxAdapter: JGraphXAdapter[T, MyEdge]) = {
    val layout = new mxCompactTreeLayout(jgxAdapter, false)

//    val layout = new mxCircleLayout(jgxAdapter)
//    val radius = 100
//
//    layout.setX0((WIDTH / 2.0) - radius)
//    layout.setY0((HEIGHT / 2.0) - radius)
//    layout.setRadius(radius)
//    layout.setMoveCircle(true)
    layout
  }

  title = "Linnaeus JGraphT Graph"
  scene = new Scene(WIDTH, HEIGHT) {
//    val jgxAdapter = newGraphAdapterFromScratch()
    val jgxAdapter = newGraphAdapterFromEidos()
    val swingNode: SwingNode = new SwingNode()
    val component = new mxGraphComponent(jgxAdapter)
    component.setConnectable(false)
    component.getGraph.setAllowDanglingEdges(false)
    swingNode.setContent(component)

//    val layout = mkLayout[String](jgxAdapter)
    val layout = mkLayout[OntologyTreeItem](jgxAdapter)
    layout.execute(jgxAdapter.getDefaultParent)

    val borderPane = new BorderPane() {
      center = swingNode
    }
    root = borderPane
  }
  show
}
