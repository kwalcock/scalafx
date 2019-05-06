package org.clulab.linnaeus.model.writer

import org.clulab.linnaeus.model.graph.eidos.EidosNetwork
import org.clulab.linnaeus.model.graph.robert.RobertNetwork
import org.clulab.linnaeus.util.Closer.AutoCloser
import org.clulab.linnaeus.util.FileUtil
import org.clulab.linnaeus.util.Tabber

class EidosWriter(val baseFilename: String) extends Writer {

  def write(network: EidosNetwork): Unit = {
    FileUtil.newPrintWriter(baseFilename + EidosWriter.FILE_END).autoClose { printWriter =>
      val tabber = new Tabber(2)

      def dumpList(depth: Int, name: String, values: Seq[String]): Unit = {
        if (values.nonEmpty) {
          printWriter.println(tabber.tab(depth, s"$name:"))
          values.foreach { value =>
            printWriter.println(tabber.tab(depth + 1, s"- $value"))
          }
        }
      }

      // There needs to be a bunch of escaping done here.
      // Just now it matches the input file except for the comments.
      // However, descriptions won't match because they span multiple lines.
      def dumpPacket(nodePacket: EidosNetwork#NodePacket, depth: Int): Unit = {
        if (nodePacket.isLeaf) {
          printWriter.println(tabber.tab(depth, "- OntologyNode:"))
          dumpList(depth + 1, "pattern", nodePacket.node.patterns)
          dumpList(depth + 1, "examples", nodePacket.node.examples)
          dumpList(depth + 1, "descriptions", nodePacket.node.descriptions)
          printWriter.println(tabber.tab(depth + 1, s"name: ${nodePacket.node.name}"))
          nodePacket.node.polarityOpt.foreach { polarity =>
            printWriter.println(tabber.tab(depth + 1, s"polarity: $polarity"))
          }
        }
        else {
          printWriter.println(tabber.tab(depth, s"- ${nodePacket.node.name}:"))
          nodePacket.outgoing.foreach { edgePacket =>
            dumpPacket(edgePacket.targetPacket, depth + 1)
          }
        }
      }

      dumpPacket(network.rootPacket, 0)
    }
  }

  // We're not converting between the two right now.
  def write(network: RobertNetwork): Unit = ???
}

object EidosWriter {
  val FILE_END = "_copy.yml"
}