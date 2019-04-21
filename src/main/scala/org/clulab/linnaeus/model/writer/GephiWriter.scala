package org.clulab.linnaeus.model.writer

import java.io.PrintWriter

import org.clulab.linnaeus.model.LinnaeusNode
import org.clulab.linnaeus.model.OntologyTreeItem
import org.clulab.linnaeus.util.Closer.AutoCloser
import org.clulab.linnaeus.util.FileUtil

class GephiWriter(val fileBasename: String) {

  protected def writeNode(item: OntologyTreeItem, printWriter: PrintWriter): Unit = {
    printWriter.print(item.id)
    printWriter.print(",")
    // Hack for CSV file
    printWriter.println(item.toString.replace(',', ';'))

    item.children.foreach(writeNode(_, printWriter))
  }

  protected def writeNode(root: OntologyTreeItem): Unit = {
    val filename = fileBasename + "_nodes.csv"

    FileUtil.newPrintWriter(filename).autoClose { printWriter =>
      printWriter.println("id,label")
      writeNode(root, printWriter)
    }
  }

  protected def writeEdge(item: OntologyTreeItem, printWriter: PrintWriter): Unit = {
    item.children.foreach { child =>
      printWriter.print(child.id)
      printWriter.print(",")
      printWriter.println(item.id)
      writeEdge(child, printWriter)
    }
  }

  protected def writeEdge(root: OntologyTreeItem): Unit = {
    val filename = fileBasename + "_edges.csv"

    FileUtil.newPrintWriter(filename).autoClose { printWriter =>
      printWriter.println("Source,Target")
      writeEdge(root, printWriter)
    }
  }

  def write(root: OntologyTreeItem): Unit = {
    writeNode(root)
    writeEdge(root)
  }

  protected def writeNode(printWriter: PrintWriter, node: LinnaeusNode.Node): Unit = {
    printWriter.print(node.data)
    printWriter.print(",")
    printWriter.println(node.data)
    node.children.foreach { child =>
      writeNode(printWriter, child)
    }
  }

  protected def writeNodes(nodes: Seq[LinnaeusNode.Node]): Unit = {
    val filename = fileBasename + "_nodes.csv"

    FileUtil.newPrintWriter(filename).autoClose { printWriter =>
      printWriter.println("id,label")
      nodes.foreach { node =>
        writeNode(printWriter, node)
      }
    }
  }

  protected def writeEdge(printWriter: PrintWriter, node: LinnaeusNode.Node): Unit = {
    node.children.foreach { child =>
      printWriter.print(node.data)
      printWriter.print(",")
      printWriter.println(child.data)
      writeEdge(printWriter, child)
    }
  }

  protected def writeEdges(nodes: Seq[LinnaeusNode.Node]): Unit = {
    val filename = fileBasename + "_edges.csv"

    FileUtil.newPrintWriter(filename).autoClose { printWriter =>
      printWriter.println("Source,Target")
      nodes.foreach { node =>
        writeEdge(printWriter, node)
      }
    }
  }

  def write(roots: Seq[LinnaeusNode.Node]): Unit = {
    writeNodes(roots)
    writeEdges(roots)
  }
}
