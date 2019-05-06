package org.clulab.linnaeus.model.writer

import org.clulab.linnaeus.model.graph.eidos.EidosNetwork
import org.clulab.linnaeus.model.graph.eidos.EidosNode
import org.clulab.linnaeus.model.graph.robert.RobertNetwork
import org.clulab.linnaeus.util.Closer.AutoCloser
import org.clulab.linnaeus.util.FileUtil
import org.clulab.linnaeus.util.Tabber

class EidosWriter(val baseFilename: String) extends Writer {

  def write(network: EidosNetwork): Unit = {
    FileUtil.newPrintWriter(baseFilename + EidosWriter.FILE_END).autoClose { printWriter =>
      val tabber = new Tabber(2)
      val visitor = new network.HierarchicalGraphVisitor()

      def dumpList(depth: Int, name: String, values: Seq[String]): Unit = {
        if (values.nonEmpty) {
          printWriter.println(tabber.tab(depth, s"$name:"))
          values.foreach { value =>
            printWriter.println(tabber.tab(depth + 1, s"- $value"))
          }
        }
      }

      def isLeaf(node: EidosNode): Boolean = {
        node.patterns.nonEmpty ||
            node.examples.nonEmpty ||
            node.descriptions.nonEmpty ||
            node.polarityOpt.nonEmpty
      }

      visitor.foreachNode { (node: EidosNode, depth: Int) =>
        if (isLeaf(node)) {
          printWriter.println(tabber.tab(depth, "- OntologyNode:"))
          dumpList(depth + 1, "pattern", node.patterns)
          dumpList(depth + 1, "examples", node.examples)
          dumpList(depth + 1, "descriptions", node.descriptions)
          printWriter.println(tabber.tab(depth + 1, s"name: ${node.name}"))
          node.polarityOpt.foreach { polarity =>
            printWriter.println(tabber.tab(depth + 1, s"polarity: $polarity"))
          }
        }
        else
          printWriter.println(tabber.tab(depth, s"- ${node.name}:"))
      }
    }
  }

  // We're not converting between the two right now.
  def write(network: RobertNetwork): Unit = ???
}

object EidosWriter {
  val FILE_END = "_copy.yml"
}