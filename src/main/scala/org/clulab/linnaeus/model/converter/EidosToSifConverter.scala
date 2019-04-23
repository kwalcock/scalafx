package org.clulab.linnaeus.model.converter

import org.clulab.linnaeus.model.EidosNode
import org.clulab.linnaeus.model.reader.EidosReader
import org.clulab.linnaeus.model.writer.SifWriter

object EidosToSifConverter {

  def convert(resourceName: String, filename: String) = {
    val root: EidosNode.Node = EidosReader.read(resourceName)

    new SifWriter(filename).writeEidos(Seq(root))
  }
}
