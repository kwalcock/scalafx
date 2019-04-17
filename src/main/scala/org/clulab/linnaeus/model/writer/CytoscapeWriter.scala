package org.clulab.linnaeus.model.writer

import java.io.PrintWriter

import org.clulab.linnaeus.model.SimpleEdge
import org.clulab.linnaeus.model.SimpleNode
import org.clulab.linnaeus.util.Closer.AutoCloser
import org.clulab.linnaeus.util.FileUtil

class CytoscapeWriter(val filename: String) {

  protected def writeNode(printWriter: PrintWriter, node: SimpleNode, isLast: Boolean): Unit = {
    printWriter.println(s"""\t{data: {id: "${node.id}"}},""")
    if (isLast)
      printWriter.println()
  }

  protected def writeEdge(printWriter: PrintWriter, edge: SimpleEdge, isLast: Boolean): Unit = {
    printWriter.print(s"""\t{data: {id: "${edge.id}", source: "${edge.sourceId}", target: "${edge.targetId}"}}""")
    if (!isLast)
      printWriter.print(",")
    printWriter.println()
  }

  def write(nodes: Seq[SimpleNode], edges: Seq[SimpleEdge]): Unit = {
    FileUtil.newPrintWriter(filename).autoClose { printWriter =>
      printWriter.println("var elements = [")
      nodes.zipWithIndex.foreach { case (node, index) => writeNode(printWriter, node, index == nodes.size - 1) }
      edges.zipWithIndex.foreach { case (edge, index) => writeEdge(printWriter, edge, index == edges.size - 1) }
      printWriter.println("];")
    }
  }
}
