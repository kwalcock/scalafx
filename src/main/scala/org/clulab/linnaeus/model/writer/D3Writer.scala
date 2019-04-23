package org.clulab.linnaeus.model.writer

import org.clulab.linnaeus.model.LinnaeusNode
import org.clulab.linnaeus.model.EidosNode
import org.clulab.linnaeus.util.Closer.AutoCloser
import org.clulab.linnaeus.util.FileUtil
import org.json4s.JObject
import org.json4s.JArray
import org.json4s.JsonDSL._
import org.json4s.jackson.JsonMethods

class D3Writer(val filename: String) {

  protected def toJArrayEidos(children: List[EidosNode.Node]): JArray = {
    new JArray(children.map { child =>
      toJObjectEidos(child)
    })
  }

  protected def toJObjectEidos(node: EidosNode.Node): JObject = {
    JObject(
      "name" -> node.toString,
      "children" -> toJArrayEidos(node.children.map(_.asInstanceOf[EidosNode.Node]).toList)
    )
  }

  def writeEidos(root: EidosNode.Node): Unit = {
    FileUtil.newPrintWriter(filename).autoClose { printWriter =>
      printWriter.println("var elements = ")
      val jObject = toJObjectEidos(root)
      val json = JsonMethods.pretty(jObject)

      printWriter.print(json)
      printWriter.println(";")
    }
  }

  protected def toJArrayLinnaeus(children: List[LinnaeusNode.Node]): JArray = {
    new JArray(children.map { child =>
      toJObjectLinnaeus(child)
    })
  }

  protected def toJObjectLinnaeus(node: LinnaeusNode.Node): JObject = {
    JObject(
      "name" -> node.toString,
      "children" -> toJArrayLinnaeus(node.children.toList)
    )
  }

  def writeLinnaeus(root: LinnaeusNode.Node): Unit = {
    FileUtil.newPrintWriter(filename).autoClose { printWriter =>
      printWriter.println("var elements = ")
      val jObject = toJObjectLinnaeus(root)
      val json = JsonMethods.pretty(jObject)

      printWriter.print(json)
      printWriter.println(";")
    }
  }
}
