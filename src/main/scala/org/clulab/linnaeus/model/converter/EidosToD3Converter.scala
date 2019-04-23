package org.clulab.linnaeus.model.converter

import org.clulab.linnaeus.model.EidosNode
import org.clulab.linnaeus.model.EidosNode
import org.clulab.linnaeus.model.reader.EidosReader
import org.clulab.linnaeus.model.writer.D3Writer

object EidosToD3Converter {

  def convert(resourceName: String, filename: String) = {
    val root: EidosNode.Node = EidosReader.read(resourceName)

    new D3Writer(filename).writeEidos(root)
  }
}
