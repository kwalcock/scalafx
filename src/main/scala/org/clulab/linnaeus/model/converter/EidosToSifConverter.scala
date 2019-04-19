package org.clulab.linnaeus.model.converter

import org.clulab.linnaeus.model.OntologyTreeItem
import org.clulab.linnaeus.model.reader.EidosReader
import org.clulab.linnaeus.model.writer.SifWriter

object EidosToSifConverter {

  def convert(filename: String) = {
    val root: OntologyTreeItem = EidosReader.read()
    new SifWriter(filename).write(root)
  }
}
