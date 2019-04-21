package org.clulab.linnaeus.model.converter

import org.clulab.linnaeus.model.reader.LinnaeusReader
import org.clulab.linnaeus.model.writer.D3Writer
import org.clulab.linnaeus.model.GraphNode

object LinnaeusToD3Converter {

  def convert(infilename: String, outfilename: String) = {
    val roots = new LinnaeusReader(infilename).read()
    val root = GraphNode.reroot(roots, "<root>")

    if (!root.isTree) {
      println("Warning: converting to tree")
      root.mkTree()
    }
    new D3Writer(outfilename).write(root)
  }
}
