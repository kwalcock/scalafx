package org.clulab.linnaeus.model.converter

import org.clulab.linnaeus.model.OntologyTreeItem
import org.clulab.linnaeus.model.reader.EidosReader
import org.clulab.linnaeus.model.writer.GephiWriter

object EidosToGephiConverter {

  def convert(fileBasename: String) = {
    val root: OntologyTreeItem = EidosReader.read()
    new GephiWriter(fileBasename).write(root)
  }
}
