package org.clulab.linnaeus.stage.graphStage

//import com.mxgraph.layout.mxCircleLayout
import com.mxgraph.layout.mxCompactTreeLayout
import com.mxgraph.swing.mxGraphComponent
import org.clulab.linnaeus.model.graph.eidos.EidosNetwork
import org.clulab.linnaeus.model.reader.EidosReader
import org.clulab.linnaeus.model.graph.eidos.EidosNode
import org.clulab.linnaeus.stage.StageManager
import org.clulab.linnaeus.util.First
//import org.jgrapht.ListenableGraph
import org.jgrapht.ext.JGraphXAdapter
import org.jgrapht.graph.DefaultDirectedGraph
import org.jgrapht.graph.DefaultEdge
import org.jgrapht.graph.DefaultListenableGraph
import scalafx.embed.swing.SwingNode
import scalafx.scene.Scene
import scalafx.scene.layout.BorderPane

class JGraphTStage(stageManager: StageManager) extends GraphStage(stageManager) {
  protected val ONTOLOGY_PATH =  "data/un_ontology.yml"

  val WIDTH = 800
  val HEIGHT = 800

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
    override def toString: String = ""
  }

  protected def newGraphAdapterFromEidos(): JGraphXAdapter[EidosNode, MyEdge] = {
    val network = new EidosReader(ONTOLOGY_PATH).read()
    val visitor = new network.HierarchicalGraphVisitor()
    val first = new First()
    val graph = new DefaultListenableGraph[EidosNode, MyEdge](new DefaultDirectedGraph[EidosNode, MyEdge](classOf[MyEdge]))
    val jgxAdapter = new JGraphXAdapter[EidosNode, MyEdge](graph)

    visitor.foreachEdge { (sourceNode, edge, targetNode) =>
      first.ifTrue { _ =>
        graph.addVertex(sourceNode)
      }
      graph.addVertex(targetNode)
      graph.addEdge(sourceNode, targetNode, new MyEdge())
    }
    jgxAdapter
  }

  protected def mkLayout[T](jgxAdapter: JGraphXAdapter[T, MyEdge]): mxCompactTreeLayout = {
//  protected def mkLayout[T](jgxAdapter: JGraphXAdapter[T, DefaultEdge]) = {
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
    val jgxAdapter: JGraphXAdapter[EidosNode, MyEdge] = newGraphAdapterFromEidos()
    val swingNode: SwingNode = new SwingNode()
    val component = new mxGraphComponent(jgxAdapter)
    component.setConnectable(false)
    component.getGraph.setAllowDanglingEdges(false)
    swingNode.setContent(component)

//    val layout = mkLayout[String](jgxAdapter)
    val layout: mxCompactTreeLayout = mkLayout[EidosNode](jgxAdapter)
    layout.execute(jgxAdapter.getDefaultParent)

    val borderPane: BorderPane = new BorderPane() {
      center = swingNode
    }
    root = borderPane
  }
  show
}
