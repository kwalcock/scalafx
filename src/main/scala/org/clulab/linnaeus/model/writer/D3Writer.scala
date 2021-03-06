package org.clulab.linnaeus.model.writer

import org.clulab.linnaeus.model.graph.eidos.EidosNetwork
import org.clulab.linnaeus.model.graph.eidos.EidosNode
import org.clulab.linnaeus.model.graph.robert.RobertNetwork
import org.clulab.linnaeus.model.graph.robert.RobertNode
import org.clulab.linnaeus.util.Closer.AutoCloser
import org.clulab.linnaeus.util.FileUtil
import org.json4s.JArray
import org.json4s.JObject
import org.json4s.JValue
import org.json4s.JsonDSL._
import org.json4s.jackson.JsonMethods

class D3Writer(val baseFilename: String) {

  /**
    * Because the json cannot be edited, this has to be done in two stages.
    * Alternatively, all the children can be processed and the results converted
    * into a composite result on the way back up.
    */
  class NamedParentNode(name: String) {
    var children: List[NamedParentNode] = List.empty

    def addChild(child: NamedParentNode): Unit = {
      children = child +: children
    }

    def toJObject: JObject = {
      JObject(
        D3Writer.NAME_LABEL -> name,
        D3Writer.CHILDREN_LABEL -> JArray {
          children.reverse.map(_.toJObject)
        }
      )
    }
  }

  def write(network: EidosNetwork): Unit = {
    FileUtil.newPrintWriter(baseFilename + D3Writer.FILE_END).autoClose { printWriter =>
      val visitor = new network.HierarchicalGraphVisitor()
      val jObject = visitor.foldUp { (node: EidosNode, children: Seq[JObject]) =>
        JObject(
          D3Writer.NAME_LABEL -> node.name,
          D3Writer.CHILDREN_LABEL -> JArray(children.toList)
        )
      }
      val json = JsonMethods.pretty(jObject)

      printWriter.print(D3Writer.HEADER)
      printWriter.print(json)
      printWriter.println(D3Writer.TRAILER)
    }
  }

  def write(network: RobertNetwork): Unit = {
    FileUtil.newPrintWriter(baseFilename + D3Writer.FILE_END).autoClose { printWriter =>
      val visitor = new network.HierarchicalGraphVisitor()
      val jObject = visitor.foldUp { (node: RobertNode, children: Seq[JObject]) =>
        JObject(
          D3Writer.NAME_LABEL -> node.getId,
          D3Writer.CHILDREN_LABEL -> JArray(children.toList)
        )
      }
      val json = JsonMethods.pretty(jObject)

      printWriter.print(D3Writer.HEADER)
      printWriter.print(json)
      printWriter.println(D3Writer.TRAILER)
    }
  }
}

object D3Writer {
  val FILE_END = "_d3.js"
  val HEADER = "var elements = "
  val TRAILER = ";"
  val NAME_LABEL = "name"
  val CHILDREN_LABEL = "children"
}
