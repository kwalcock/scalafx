package org.clulab.linnaeus.model.converter

import org.clulab.linnaeus.model.reader.RobertReader
import org.clulab.linnaeus.model.writer.RobertWriter

class RobertToRobertConverter(inFilename: String) extends Converter(inFilename) {

  def convert(): Unit = {
    val network = new RobertReader(inFilename).read()

    new RobertWriter(baseFilename).write(network)
  }
}
