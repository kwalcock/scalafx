package org.clulab.linnaeus.model.writer

import org.clulab.linnaeus.model.graph.eidos.EidosNetwork
import org.clulab.linnaeus.model.graph.robert.RobertNetwork
import org.clulab.linnaeus.util.Closer.AutoCloser
import org.clulab.linnaeus.util.FileUtil
import org.json4s.JArray
import org.json4s.JObject
import org.json4s.JsonDSL._
import org.json4s.jackson.JsonMethods

class D3Writer(val baseFilename: String) {

  def write(network: EidosNetwork): Unit = {
    FileUtil.newPrintWriter(baseFilename + D3Writer.FILE_END).autoClose { printWriter =>

      def toJArray(children: Seq[EidosNetwork#NodePacket]): JArray = {
        new JArray(children.toList.map { child =>
          toJObject(child)
        })
      }

      def toJObject(nodePacket: EidosNetwork#NodePacket): JObject = {
        JObject(
          D3Writer.NAME_LABEL -> nodePacket.node.name,
          D3Writer.CHILDREN_LABEL -> toJArray(nodePacket.outgoing.map(_.targetPacket))
        )
      }

      printWriter.print(D3Writer.HEADER)
      val jObject = toJObject(network.rootPacket)
      val json = JsonMethods.pretty(jObject)

      printWriter.print(json)
      printWriter.println(D3Writer.TRAILER)
    }
  }

  def write(network: RobertNetwork): Unit = {
    FileUtil.newPrintWriter(baseFilename + D3Writer.FILE_END).autoClose { printWriter =>

      def toJArray(children: Seq[RobertNetwork#NodePacket]): JArray = {
        new JArray(children.toList.map { child =>
          toJObject(child)
        })
      }

      def toJObject(nodePacket: RobertNetwork#NodePacket): JObject = {
        JObject(
          D3Writer.NAME_LABEL -> nodePacket.node.getId,
          D3Writer.CHILDREN_LABEL -> toJArray(nodePacket.outgoing.map(_.targetPacket))
        )
      }

      printWriter.print(D3Writer.HEADER)
      val jObject = toJObject(network.rootPacket)
      val json = JsonMethods.pretty(jObject)

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
