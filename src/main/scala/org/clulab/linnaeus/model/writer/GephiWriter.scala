package org.clulab.linnaeus.model.writer

import java.io.PrintWriter

import org.clulab.linnaeus.model.LinnaeusNode
import org.clulab.linnaeus.model.EidosNode
import org.clulab.linnaeus.util.Closer.AutoCloser
import org.clulab.linnaeus.util.FileUtil

class GephiWriter(val fileBasename: String) {

  protected def writeNodeEidos(printWriter: PrintWriter, node: EidosNode.Node): Unit = {
    printWriter.print(node.getId)
    printWriter.print(",")
    // Hack for CSV file
    printWriter.println(node.toString.replace(',', ';'))
  }

  protected def writeEdgesEidos(printWriter: PrintWriter, node: EidosNode.Node): Unit = {
    node.children.foreach { child =>
      printWriter.print(child.asInstanceOf[EidosNode.Node].getId)
      printWriter.print(",")
      printWriter.println(node.getId)
    }
  }

  protected def writeNodeAndEdgesEidos(nodePrintWriter: PrintWriter, edgePrintWriter: PrintWriter)(node: EidosNode.Node): Boolean = {
    writeNodeEidos(nodePrintWriter, node)
    writeEdgesEidos(edgePrintWriter, node)
    false
  }

  def writeEidos(roots: Seq[EidosNode.Node]): Unit = {
    val nodeFilename = fileBasename + "_nodes.csv"
    val edgeFilename = fileBasename + "_edges.csv"

    FileUtil.newPrintWriter(nodeFilename).autoClose { nodePrintWriter =>
      nodePrintWriter.println("id,label")
      FileUtil.newPrintWriter(edgeFilename).autoClose { edgePrintWriter =>
        edgePrintWriter.println ("Source,Target")
        roots.foreach { root => root.foreach(writeNodeAndEdgesEidos(nodePrintWriter, edgePrintWriter)) }
      }
    }
  }

  protected def writeNodeLinnaeus(printWriter: PrintWriter, node: LinnaeusNode.Node): Unit = {
    printWriter.print(node.data)
    printWriter.print(",")
    printWriter.println(node.data)
  }

  protected def writeEdgesLinnaeus(printWriter: PrintWriter, node: LinnaeusNode.Node): Unit = {
    node.children.foreach { child =>
      printWriter.print(node.data)
      printWriter.print(",")
      printWriter.println(child.data)
    }
  }

  protected def writeNodeAndEdgesLinnaeus(nodePrintWriter: PrintWriter, edgePrintWriter: PrintWriter)(node: LinnaeusNode.Node): Boolean = {
    writeNodeLinnaeus(nodePrintWriter, node)
    writeEdgesLinnaeus(edgePrintWriter, node)
    false
  }

  def writeLinnaeus(roots: Seq[LinnaeusNode.Node]): Unit = {
    val nodeFilename = fileBasename + "_nodes.csv"
    val edgeFilename = fileBasename + "_edges.csv"

    FileUtil.newPrintWriter(nodeFilename).autoClose { nodePrintWriter =>
      nodePrintWriter.println("id,label")
      FileUtil.newPrintWriter(edgeFilename).autoClose { edgePrintWriter =>
        edgePrintWriter.println("Source,Target")
        roots.foreach { root => root.foreach(writeNodeAndEdgesLinnaeus(nodePrintWriter, edgePrintWriter)) }
      }
    }
  }
}
