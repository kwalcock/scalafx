package org.clulab.linnaeus.model.converter

import org.clulab.linnaeus.model.graph.eidos.EidosNetwork
import org.clulab.linnaeus.model.reader.EidosReader
import org.clulab.linnaeus.model.writer.D3Writer

class EidosToD3Converter(inFilename: String) extends Converter(inFilename) {

  def convert(): Unit = {
    val network: EidosNetwork = new EidosReader(inFilename).read()

    new D3Writer(baseFilename).write(network)
  }
}
