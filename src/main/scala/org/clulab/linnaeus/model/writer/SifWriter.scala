package org.clulab.linnaeus.model.writer

import org.clulab.linnaeus.model.graph.eidos.EidosNetwork
import org.clulab.linnaeus.model.graph.robert.RobertNetwork
import org.clulab.linnaeus.util.Closer.AutoCloser
import org.clulab.linnaeus.util.FileUtil

class SifWriter(val filename: String) {

  def write(network: EidosNetwork): Unit = {
    FileUtil.newPrintWriter(filename + SifWriter.FILE_END).autoClose { printWriter =>
      val visitor = new network.LinearGraphVisitor()

      visitor.foreachEdge { (source, edge, target) =>
        printWriter.print(source.name.replace(' ', '_'))
        printWriter.print(s" is_the_parent_of ")
        printWriter.println(target.name.replace(' ', '_'))
      }
    }
  }

  def write(network: RobertNetwork): Unit = {
    FileUtil.newPrintWriter(filename + SifWriter.FILE_END).autoClose { printWriter =>
      val visitor = new network.LinearGraphVisitor()

      visitor.foreachEdge { (source, edge, target) =>
        printWriter.print(source.getId.replace(' ', '_'))
        printWriter.print(s" is_a_hypernym_of ")
        printWriter.println(target.getId.replace(' ', '_'))
      }
    }
  }
}

object SifWriter {
  val FILE_END = "_copy.sif"
}