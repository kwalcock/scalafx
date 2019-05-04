package org.clulab.linnaeus.model.converter

import org.clulab.linnaeus.model.reader.EidosReader
import org.clulab.linnaeus.model.writer.CytoscapeWriter

class EidosToCytoscapeConverter(inFilename: String) extends Converter(inFilename) {

  def convert(): Unit = {
    val network = new EidosReader(inFilename).read()

    new CytoscapeWriter(baseFilename).write(network)
  }
}
