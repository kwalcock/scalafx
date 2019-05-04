package org.clulab.linnaeus.model.converter

import org.clulab.linnaeus.model.reader.RobertReader
import org.clulab.linnaeus.model.writer.CytoscapeWriter

class RobertToCytoscapeConverter(inFilename: String) extends Converter(inFilename) {

  def convert(): Unit = {
    val network = new RobertReader(inFilename).read()

    new CytoscapeWriter(baseFilename).write(network)
  }
}
