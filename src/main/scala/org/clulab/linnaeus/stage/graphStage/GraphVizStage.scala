package org.clulab.linnaeus.stage.graphStage

import java.io.File

import guru.nidi.graphviz.attribute.RankDir
import guru.nidi.graphviz.engine.Format
import guru.nidi.graphviz.engine.Graphviz
import guru.nidi.graphviz.model.Factory
import guru.nidi.graphviz.model.Graph
import org.clulab.linnaeus.model.graph.eidos.EidosNetwork
import org.clulab.linnaeus.model.reader.EidosReader
import org.clulab.linnaeus.stage.StageManager
import org.clulab.linnaeus.util.First
import scalafx.scene.image.Image
import scalafx.scene.image.ImageView
import scalafx.scene.Scene
import scalafx.scene.control.ScrollPane

class GraphVizStage(stageManager: StageManager) extends GraphStage(stageManager) {
  protected val ONTOLOGY_PATH =  "data/un_ontology.yml"

  val WIDTH = 800
  val HEIGHT = 800

  protected def mkGraphFromEidos(): Graph = {
    val network = new EidosReader(ONTOLOGY_PATH).read()
    val visitor = new network.HierarchicalGraphVisitor()
    val first = new First()
    var graph = Factory
        .graph("example1")
        .directed()
        .graphAttr()
        .`with`(RankDir.LEFT_TO_RIGHT)

    visitor.foreachEdge { (sourceNode, edge, targetNode) =>
      first.ifTrue { _ =>
        graph = graph.`with`(Factory.node(sourceNode.name))
      }
      graph = graph.`with`(Factory.node(targetNode.name).link(Factory.node(sourceNode.name)))
    }
    graph
  }

  title = "Linnaeus GraphViz Graph"
  scene = new Scene(WIDTH, HEIGHT) {
    val graph: Graph = mkGraphFromEidos()
    val visual: Unit = Graphviz.fromGraph(graph)/*.height(HEIGHT)*/.render(Format.PNG).toFile(new File("ex1.png"))
    val image = new Image("file:ex1.png")
    val imageView = new ImageView(image)
    val scrollPane = new ScrollPane()
    scrollPane.content = imageView
    root = scrollPane
  }
  show
}
