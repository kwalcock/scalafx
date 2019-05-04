package org.clulab.linnaeus.stage.graphStage

import java.io.File

import guru.nidi.graphviz.attribute.Color
import guru.nidi.graphviz.attribute.RankDir
import guru.nidi.graphviz.engine.Format
import guru.nidi.graphviz.engine.Graphviz
import guru.nidi.graphviz.model.Factory
import guru.nidi.graphviz.model.Graph
import org.clulab.linnaeus.model.reader.EidosReader
import org.clulab.linnaeus.model.graph.eidos.EidosNode
import org.clulab.linnaeus.stage.StageManager
import scalafx.Includes._
import scalafx.scene.image.Image
import scalafx.scene.image.ImageView
import scalafx.scene.Scene
import scalafx.scene.control.ScrollPane

class GraphVizStage(stageManager: StageManager) extends GraphStage(stageManager) {
  protected val ONTOLOGY_PATH =  "data/un_ontology.yml"

  val WIDTH = 800
  val HEIGHT = 800

  protected def getGraphFromEidos(): Graph = {
    val network = new EidosReader(ONTOLOGY_PATH).read()
    val rootRecord = network.rootRecord
    var graph = Factory
        .graph("example1")
        .directed()
        .graphAttr()
        .`with`(RankDir.LEFT_TO_RIGHT)

    def addChildren(parentRecord: network.NodeRecord, remaining: Int): Unit = {
      if (remaining > 0)
        parentRecord.outgoing.map(_.targetRecord).foreach { childRecord =>
          graph = graph.`with`(Factory.node(childRecord.node.name).link(Factory.node(parentRecord.node.name)))
          addChildren(childRecord, remaining - 1)
        }
    }

    // What if strings are not unique?
    graph = graph.`with`(Factory.node(rootRecord.node.name))
    addChildren(rootRecord, 100)
    graph
  }

  title = "Linnaeus GraphViz Graph"
  scene = new Scene(WIDTH, HEIGHT) {
    val graph = getGraphFromEidos()
    val visual = Graphviz.fromGraph(graph)/*.height(HEIGHT)*/.render(Format.PNG).toFile(new File("ex1.png"))
    val image = new Image("file:ex1.png")
    val imageView = new ImageView(image)
    val scrollPane = new ScrollPane()
    scrollPane.content = imageView
    root = scrollPane
  }
  show
}
