package org.clulab.linnaeus.model.converter

import org.clulab.linnaeus.model.reader.RobertReader
import org.clulab.linnaeus.model.writer.GephiWriter

class RobertToGephiConverter(inFilename: String) extends Converter(inFilename) {

  def convert(): Unit = {
    val network = new RobertReader(inFilename).read()

    if (!network.isTree)
      println("Warning: network is not tree!")
    new GephiWriter(baseFilename).write(network)
  }
}
