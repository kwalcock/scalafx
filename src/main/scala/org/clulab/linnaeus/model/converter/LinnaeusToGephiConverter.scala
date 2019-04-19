package org.clulab.linnaeus.model.converter

import org.clulab.linnaeus.model.reader.LinnaeusReader
import org.clulab.linnaeus.model.writer.GephiWriter

object LinnaeusToGephiConverter {

  def convert(infilename: String, outfileBasename: String) = {
    val (nodes, edges) = new LinnaeusReader(infilename).read()

    new GephiWriter(outfileBasename).write(nodes, edges)
  }
}
