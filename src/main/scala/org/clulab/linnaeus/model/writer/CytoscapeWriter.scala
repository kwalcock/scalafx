package org.clulab.linnaeus.model.writer

import java.io.PrintWriter

import org.clulab.linnaeus.model.LinnaeusNode
import org.clulab.linnaeus.util.Closer.AutoCloser
import org.clulab.linnaeus.util.FileUtil

class CytoscapeWriter(val filename: String) {

  // TODO: This is JSON, so process it as such.  If not, then need to escape properly.
  protected def writeNode(printWriter: PrintWriter, node: LinnaeusNode.Node, isFirst: Boolean): Unit = {
    if (!isFirst)
      printWriter.println(",")
    printWriter.print(s"""\t{data: {id: "${node.value}"}}""")
    node.children.foreach { child =>
      val id = node.value + "_" + child.value

      writeNode(printWriter, child, false)
      printWriter.print(s""",\n\t{data: {id: "${id}", source: "${node.value}", target: "${child.value}"}}""")
    }
  }

  def writeLinnaeus(roots: Seq[LinnaeusNode.Node]): Unit = {
    FileUtil.newPrintWriter(filename).autoClose { printWriter =>
      printWriter.println("var elements = [")
      roots.zipWithIndex.foreach { case (root, index) => writeNode(printWriter, root, index == 0) }
      printWriter.println("\n];")
    }
  }
}
