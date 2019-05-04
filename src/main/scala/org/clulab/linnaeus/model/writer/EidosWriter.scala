package org.clulab.linnaeus.model.writer

import org.clulab.linnaeus.model.graph.eidos.EidosNetwork
import org.clulab.linnaeus.model.graph.robert.RobertNetwork
import org.clulab.linnaeus.util.Closer.AutoCloser
import org.clulab.linnaeus.util.FileUtil

class EidosWriter(val baseFilename: String) extends Writer {

  def write(network: EidosNetwork): Unit = {
    FileUtil.newPrintWriter(baseFilename + EidosWriter.FILE_END).autoClose { printWriter =>

      def tab(depth: Int): Unit = {
        0.until(depth * 2).foreach(_ => printWriter.print(' '))
      }

      def dumpList(depth: Int, name: String, values: Seq[String]): Unit = {
        if (values.nonEmpty) {
          tab(depth); printWriter.println(s"$name:")

          values.foreach { value =>
            tab(depth + 1); printWriter.println(s"- $value")
          }
        }
      }

      // There needs to be a bunch of escaping done here.
      // Just now it matches the input file except for the comments.
      // However, descriptions won't match because they span multiple lines.
      def dumpRecord(nodeRecord: network.NodeRecord, depth: Int): Unit = {
        if (nodeRecord.isLeaf) {
          tab(depth); printWriter.println("- OntologyNode:")
          dumpList(depth + 1, "pattern", nodeRecord.node.patterns)
          dumpList(depth + 1, "examples", nodeRecord.node.examples)
          dumpList(depth + 1, "descriptions", nodeRecord.node.descriptions)
          tab(depth + 1); printWriter.println(s"name: ${nodeRecord.node.name}")
          nodeRecord.node.polarityOpt.foreach { polarity =>
            tab(depth + 1); printWriter.println(s"polarity: $polarity")
          }
        }
        else {
          tab(depth); printWriter.println(s"- ${nodeRecord.node.name}:")
          nodeRecord.outgoing.foreach { edgeRecord =>
            dumpRecord(edgeRecord.targetRecord, depth + 1)
          }
        }
      }

      dumpRecord(network.rootRecord, 0)
    }
  }

  // We're not converting between the two right now.
  def write(network: RobertNetwork): Unit = ???
}

object EidosWriter {
  val FILE_END = "_copy.yml"
}