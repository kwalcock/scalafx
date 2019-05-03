package org.clulab.linnaeus.model.writer

import org.clulab.linnaeus.model.graph.eidos.EidosNetwork
import org.clulab.linnaeus.model.graph.robert.RobertNetwork
import org.clulab.linnaeus.util.Closer.AutoCloser
import org.clulab.linnaeus.util.FileUtil
import org.clulab.linnaeus.util.First

class RobertWriter(val baseFilename: String) {

  // We're not converting between the two right now.
  def write(network: EidosNetwork): Unit = ???

  def write(network: RobertNetwork): Unit = {
    FileUtil.newPrintWriter(baseFilename + RobertWriter.FILE_END).autoClose { printWriter =>
      val first = new First()

      network.foreachNodeInLinearOrder { node =>
        first.ifFalse(_ => printWriter.print('\t'))
        printWriter.print(node.getId)
      }
      printWriter.println()

      network.foreachEdgeInLinearOrder { (source, edge, target) =>
        printWriter.print(s"${source.getId}\t${target.getId}")
        edge.weightOpt.foreach { weight =>
          printWriter.print(s"\t$weight")
        }
        printWriter.println()
      }
    }
  }
}

object RobertWriter {
  val FILE_END = "_copy.tax"
}