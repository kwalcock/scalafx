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
      val namedParentNode = visitor.fold(Option.empty[NamedParentNode]) { (namedParentNodeOpt: Option[NamedParentNode], node: EidosNode) =>
        val result = new NamedParentNode(node.name)

        namedParentNodeOpt.foreach { namedParentNode =>
          namedParentNode.addChild(result)
        }
        Some(result)
      }.get

      val jObject = namedParentNode.toJObject
      val json = JsonMethods.pretty(jObject)

      printWriter.print(D3Writer.HEADER)
      printWriter.print(json)
      printWriter.println(D3Writer.TRAILER)
    }
  }

  def write(network: RobertNetwork): Unit = {
    FileUtil.newPrintWriter(baseFilename + D3Writer.FILE_END).autoClose { printWriter =>
      val visitor = new network.HierarchicalGraphVisitor()
      val jObject = visitor.fold(Option.empty[JObject]) { (parentJObjectOpt: Option[JObject], node: RobertNode) =>
        val result = JObject(
          D3Writer.NAME_LABEL -> node.getId,
          // This can probably be delayed until there really are children.
          D3Writer.CHILDREN_LABEL -> new JArray(List.empty[JValue])
        )

        parentJObjectOpt.foreach { parentJObject =>
          (parentJObject \ D3Writer.CHILDREN_LABEL) ++ new JArray(List(result))
        }
        Some(result)
      }.get
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
