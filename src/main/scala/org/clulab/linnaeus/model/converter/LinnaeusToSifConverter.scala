package org.clulab.linnaeus.model.converter

import org.clulab.linnaeus.model.reader.LinnaeusReader
import org.clulab.linnaeus.model.writer.SifWriter

object LinnaeusToSifConverter {

  def convert(infilename: String, outfilename: String) = {
    val (nodes, edges) = new LinnaeusReader(infilename).read()

    new SifWriter(outfilename).write(nodes, edges)
  }
}
