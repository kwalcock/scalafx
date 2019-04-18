package org.clulab.linnaeus.model.writer

import org.clulab.linnaeus.model.LinnaeusNode
import org.clulab.linnaeus.model.OntologyTreeItem
import org.clulab.linnaeus.util.Closer.AutoCloser
import org.clulab.linnaeus.util.FileUtil
import org.json4s.JObject
import org.json4s.JArray
import org.json4s.JsonDSL._
import org.json4s.jackson.JsonMethods

class D3Writer(val filename: String) {

  protected def toJArray(children: List[OntologyTreeItem]): JArray = {
    new JArray(children.map { child =>
      toJObject(child)
    })
  }

  protected def toJObject(node: OntologyTreeItem): JObject = {
    JObject(
      "name" -> node.toString,
      "children" -> toJArray(node.children.toList)
    )
  }

  def write(root: OntologyTreeItem): Unit = {
    FileUtil.newPrintWriter(filename).autoClose { printWriter =>
      printWriter.println("var elements = ")
      val jObject = toJObject(root)
      val json = JsonMethods.pretty(jObject)

      printWriter.print(json)
      printWriter.println(";")
    }
  }

  protected def toJArrayB(children: List[LinnaeusNode]): JArray = {
    new JArray(children.map { child =>
      toJObject(child)
    })
  }

  protected def toJObject(node: LinnaeusNode): JObject = {
    JObject(
      "name" -> node.toString,
      "children" -> toJArrayB(node.children.toList)
    )
  }

  def write(root: LinnaeusNode): Unit = {
    FileUtil.newPrintWriter(filename).autoClose { printWriter =>
      printWriter.println("var elements = ")
      val jObject = toJObject(root)
      val json = JsonMethods.pretty(jObject)

      printWriter.print(json)
      printWriter.println(";")
    }
  }
}
