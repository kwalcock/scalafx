package org.clulab.linnaeus.model.writer

import java.io.PrintWriter

import org.clulab.linnaeus.model.LinnaeusNode
import org.clulab.linnaeus.model.EidosNode
import org.clulab.linnaeus.util.Closer.AutoCloser
import org.clulab.linnaeus.util.FileUtil

class SifWriter(val filename: String) {

  protected def writeEdgeEidos(printWriter: PrintWriter, item: EidosNode.Node): Unit = {
    item.children.foreach { child =>
      printWriter.print(item.toString.replace(' ', '_'))
      printWriter.print(" is_a_hypernym_of ")
      printWriter.println(child.toString.replace(' ', '_'))
      writeEdgeEidos(printWriter, child.asInstanceOf[EidosNode.Node])
    }
  }

  protected def writeEdgeEidos(root: EidosNode.Node): Unit = {
    FileUtil.newPrintWriter(filename).autoClose { printWriter =>
      writeEdgeEidos(printWriter, root)
    }
  }

  def writeEidos(roots: Seq[EidosNode.Node]): Unit = {
    writeEdgeEidos(roots.head)
  }

  protected def writeNodeLinnaeus(printWriter: PrintWriter)(node: LinnaeusNode.Node): Boolean = {
    node.children.foreach { child =>
      printWriter.print(node.value.replace(' ', '_'))
      printWriter.print(" is_a_hypernym_of ")
      printWriter.println(child.value.replace(' ', '_'))
    }
    false
  }

  def writeLinnaeus(roots: Seq[LinnaeusNode.Node]): Unit = {
    FileUtil.newPrintWriter(filename).autoClose { printWriter =>
      roots.foreach { root =>
        root.foreach(writeNodeLinnaeus(printWriter))
      }
    }
  }
}
