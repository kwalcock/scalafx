package org.clulab.linnaeus.model.converter

import org.clulab.linnaeus.model.reader.RobertReader
import org.clulab.linnaeus.model.writer.SifWriter

class RobertToSifConverter(inFilename: String) extends Converter(inFilename) {

  def convert: Unit = {
    val network = new RobertReader(inFilename).read()

    new SifWriter(baseFilename).write(network)
  }
}
