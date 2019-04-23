package org.clulab.linnaeus.model.converter

import org.clulab.linnaeus.model.EidosNode
import org.clulab.linnaeus.model.reader.EidosReader
import org.clulab.linnaeus.model.writer.GephiWriter

object EidosToGephiConverter {

  def convert(resourceName: String, fileBasename: String) = {
    val root: EidosNode.Node = EidosReader.read(resourceName)

    new GephiWriter(fileBasename).writeEidos(Seq(root))
  }
}
