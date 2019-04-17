package org.clulab.linnaeus.model.converter

import org.clulab.linnaeus.model.OntologyTreeItem
import org.clulab.linnaeus.model.reader.EidosReader
import org.clulab.linnaeus.model.writer.D3Writer

object EidosToD3Converter {

  def convert(filename: String) = {
    val root: OntologyTreeItem = EidosReader.read()
    new D3Writer(filename).write(root)
  }
}
