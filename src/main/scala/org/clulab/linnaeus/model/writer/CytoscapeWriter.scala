package org.clulab.linnaeus.model.writer

import org.clulab.linnaeus.model.graph.eidos.EidosNetwork
import org.clulab.linnaeus.model.graph.robert.RobertNetwork
import org.clulab.linnaeus.util.Closer.AutoCloser
import org.clulab.linnaeus.util.FileUtil
import org.json4s.JArray
import org.json4s.JField
import org.json4s.JObject
import org.json4s.JsonDSL._
import org.json4s.jackson.JsonMethods

class CytoscapeWriter(val filename: String) {

  protected def nodesToJArray(network: EidosNetwork): JArray = {
    val visitor = new network.LinearGraphVisitor()

    JArray(
      visitor.mapNode { node =>
        JObject(
          "data" -> JObject(
            "id" -> node.getId,
            "label" -> node.name
          )
        )
      }.toList
    )
  }

  protected def edgesToJArray(network: EidosNetwork): JArray = {
    val nextNodeId = network.nodeIndexer.next
    val visitor = new network.LinearGraphVisitor()

    JArray(
      visitor.mapEdge { (source, edge, target) =>
        JObject(
          "data" -> JObject(
            // Cytoscape IDs are shared between nodes and edges.
            "id" -> (nextNodeId + edge.getId),
            "source" -> source.getId,
            "target" -> target.getId
          )
        )
      }.toList
    )
  }

  def write(network: EidosNetwork): Unit = {
    FileUtil.newPrintWriter(filename + CytoscapeWriter.FILE_END).autoClose { printWriter =>
      printWriter.println(CytoscapeWriter.HEADER)

      val nodeJArray = nodesToJArray(network)
      val edgeJArray = edgesToJArray(network)
      val json = JsonMethods.pretty(nodeJArray ++ edgeJArray)

      printWriter.print(json)
      printWriter.println(CytoscapeWriter.TRAILER)
    }
  }

  protected def nodesToJArray(network: RobertNetwork): JArray = {
    val visitor = new network.LinearGraphVisitor()

    JArray(
      visitor.mapNode { node =>
        JObject(
          "data" -> JObject(JField("id", node.getId))
        )
      }.toList
    )
  }

  protected def edgesToJArray(network: RobertNetwork): JArray = {
    val visitor = new network.LinearGraphVisitor()

    JArray(
      visitor.mapEdge { (source, edge, target) =>
        JObject(
          "data" -> JObject(
            "id" -> edge.getId,
            "source" -> source.getId,
            "target" -> target.getId
          )
        )
      }.toList
    )
  }

  def write(network: RobertNetwork): Unit = {
    FileUtil.newPrintWriter(filename + CytoscapeWriter.FILE_END).autoClose { printWriter =>
      printWriter.println(CytoscapeWriter.HEADER)

      val visitor = new network.LinearGraphVisitor
      val nodeJArray = nodesToJArray(network)
      val edgeJArray = edgesToJArray(network)
      val json = JsonMethods.pretty(nodeJArray ++ edgeJArray)

      printWriter.print(json)
      printWriter.println(CytoscapeWriter.TRAILER)
    }
  }
}

object CytoscapeWriter {
  val FILE_END = "_cy.js"
  val HEADER = "var elements = "
  val TRAILER = ";"
  val NAME_LABEL = "name"
  val CHILDREN_LABEL = "children"
}
