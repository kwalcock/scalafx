package org.clulab.linnaeus.model.converter

import org.clulab.linnaeus.model.DirectedGraphNode
import org.clulab.linnaeus.model.reader.LinnaeusReader
import org.clulab.linnaeus.model.writer.GephiWriter

object LinnaeusToGephiConverter {

  def convert(infilename: String, outfileBasename: String) = {
    val roots = new LinnaeusReader(infilename).read()
    val root = DirectedGraphNode.reroot(roots, "<root>")

    if (!root.isTree) {
      println("Warning: converting to tree")
      root.mkTree()
    }
    new GephiWriter(outfileBasename).writeLinnaeus(Seq(root))
  }
}
