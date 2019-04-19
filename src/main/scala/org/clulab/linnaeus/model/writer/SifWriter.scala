package org.clulab.linnaeus.model.writer

import java.io.PrintWriter

import org.clulab.linnaeus.model.OntologyTreeItem
import org.clulab.linnaeus.model.SimpleEdge
import org.clulab.linnaeus.model.SimpleNode
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

  protected def writeEdge(edge: SimpleEdge, printWriter: PrintWriter): Unit = {
    printWriter.print(edge.sourceId.replace(' ', '_'))
    printWriter.print(" is_a_hypernym_of ")
    printWriter.println(edge.targetId.replace(' ', '_'))
  }

  protected def writeEdges(edges: Seq[SimpleEdge]): Unit = {
    FileUtil.newPrintWriter(filename).autoClose { printWriter =>
      edges.foreach { edge =>
        writeEdge(edge, printWriter)
      }
    }
  }

  def write(nodes: Seq[SimpleNode], edges: Seq[SimpleEdge]): Unit = {
    writeEdges(edges)
  }
}
