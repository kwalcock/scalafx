package org.clulab.linnaeus.model.converter

import org.clulab.linnaeus.model.OntologyTreeItem
import org.clulab.linnaeus.model.reader.EidosReader
import org.clulab.linnaeus.model.writer.GephiWriter

object EidosToGephiConverter {

  def convert() = {
    val root: OntologyTreeItem = EidosReader.read()
    new GephiWriter("test").write(root)
  }
}
