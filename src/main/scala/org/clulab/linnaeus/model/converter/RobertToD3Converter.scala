package org.clulab.linnaeus.model.converter

import org.clulab.linnaeus.model.writer.D3Writer
import org.clulab.linnaeus.model.reader.RobertReader

class RobertToD3Converter(inFilename: String) extends Converter(inFilename) {

  def convert(): Unit = {
    val network = new RobertReader(inFilename).read()

    if (!network.isTree) {
      println("Warning: converting to tree")
      network.mkTree()
    }

    new D3Writer(baseFilename).write(network)
  }
}
