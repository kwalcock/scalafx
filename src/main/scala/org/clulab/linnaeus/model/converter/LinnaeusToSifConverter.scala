package org.clulab.linnaeus.model.converter

import org.clulab.linnaeus.model.reader.LinnaeusReader
import org.clulab.linnaeus.model.writer.SifWriter

object LinnaeusToSifConverter {

  def convert(infilename: String, outfilename: String, hasWeights: Boolean) = {
    val roots = new LinnaeusReader(infilename, hasWeights).read()

    new SifWriter(outfilename).writeLinnaeus(roots)
  }
}
