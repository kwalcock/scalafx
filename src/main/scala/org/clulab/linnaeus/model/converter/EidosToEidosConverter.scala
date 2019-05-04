package org.clulab.linnaeus.model.converter

import org.clulab.linnaeus.model.graph.eidos.EidosNetwork
import org.clulab.linnaeus.model.reader.EidosReader
import org.clulab.linnaeus.model.writer.EidosWriter

class EidosToEidosConverter(inFilename: String) extends Converter(inFilename) {

  def convert(): Unit = {
    val network: EidosNetwork = new EidosReader(inFilename).read()

    new EidosWriter(baseFilename).write(network)
  }
}
