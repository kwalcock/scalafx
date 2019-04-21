package org.clulab.linnaeus.model.writer

import java.io.PrintWriter

import org.clulab.linnaeus.model.LinnaeusNode
import org.clulab.linnaeus.model.OntologyTreeItem
import org.clulab.linnaeus.util.Closer.AutoCloser
import org.clulab.linnaeus.util.FileUtil

class SifWriter(val filename: String) {

  protected def writeEdge(item: OntologyTreeItem, printWriter: PrintWriter): Unit = {
    item.children.foreach { child =>
      printWriter.print(item.toString.replace(' ', '_'))
      printWriter.print(" is_a_hypernym_of ")
      printWriter.println(child.toString.replace(' ', '_'))
      writeEdge(child, printWriter)
    }
  }

  protected def writeEdge(root: OntologyTreeItem): Unit = {
    FileUtil.newPrintWriter(filename).autoClose { printWriter =>
      writeEdge(root, printWriter)
    }
  }

  def write(root: OntologyTreeItem): Unit = {
    writeEdge(root)
  }

  protected def writeEdge(printWriter: PrintWriter, node: LinnaeusNode.Node): Unit = {
    node.children.foreach { child =>
      printWriter.print(node.data.replace(' ', '_'))
      printWriter.print(" is_a_hypernym_of ")
      printWriter.println(child.data.replace(' ', '_'))
    }
  }

  def write(roots: Seq[LinnaeusNode.Node]): Unit = {
    FileUtil.newPrintWriter(filename).autoClose { printWriter =>
      roots.foreach { root =>
        writeEdge(printWriter, root)
      }
    }
  }
}
