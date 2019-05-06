package org.clulab.linnaeus.model.writer

import org.clulab.linnaeus.model.graph.eidos.EidosNetwork
import org.clulab.linnaeus.model.graph.robert.RobertNetwork
import org.clulab.linnaeus.util.Closer.AutoCloser
import org.clulab.linnaeus.util.FileUtil

class GephiWriter(val fileBasename: String) extends Writer {

  protected def writeNodes(filename: String, network: EidosNetwork): Unit = {
    val visitor = new network.LinearGraphVisitor()

    FileUtil.newPrintWriter(filename).autoClose { printWriter =>
      printWriter.println(GephiWriter.NODE_HEADER)
      visitor.foreachNode { node =>
        printWriter.println(s"${node.getId}\t${node.name}")
      }
    }
  }

  protected def writeEdges(filename: String, network: EidosNetwork): Unit = {
    val visitor = new network.LinearGraphVisitor()

    FileUtil.newPrintWriter(filename).autoClose { printWriter =>
      printWriter.println(GephiWriter.EDGE_HEADER)
      visitor.foreachEdge { (source, edge, target) =>
        printWriter.println(s"${source.getId}\t${target.getId}")
      }
    }
  }

  def write(network: EidosNetwork): Unit = {
    writeNodes(fileBasename + GephiWriter.NODE_END, network)
    writeEdges(fileBasename + GephiWriter.EDGE_END, network)
  }

  protected def writeNodes(filename: String, network: RobertNetwork): Unit = {
    val visitor = new network.LinearGraphVisitor()

    FileUtil.newPrintWriter(filename).autoClose { printWriter =>
      printWriter.println(GephiWriter.NODE_HEADER)
      visitor.foreachNode { node =>
        printWriter.println(s"${node.getId}\t${node.getId}")
      }
    }
  }

  protected def writeEdges(filename: String, network: RobertNetwork): Unit = {
    val visitor = new network.LinearGraphVisitor()

    FileUtil.newPrintWriter(filename).autoClose { printWriter =>
      printWriter.println(GephiWriter.EDGE_HEADER)
      visitor.foreachEdge { (source, edge, target) =>
        printWriter.println(s"${source.getId}\t${target.getId}")
      }
    }
  }

  def write(network: RobertNetwork): Unit = {
    writeNodes(fileBasename + GephiWriter.NODE_END, network)
    writeEdges(fileBasename + GephiWriter.EDGE_END, network)
  }
}

object GephiWriter {
  val NODE_END = "_nodes.tsv"
  val EDGE_END = "_edges.tsv"

  val NODE_HEADER = "id\tlabel"
  val EDGE_HEADER = "Source\tTarget"
}