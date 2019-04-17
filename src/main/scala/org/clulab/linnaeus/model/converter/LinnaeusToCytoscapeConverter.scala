package org.clulab.linnaeus.model.converter

import org.clulab.linnaeus.model.reader.LinnaeusReader
import org.clulab.linnaeus.model.writer.CytoscapeWriter

object LinnaeusToCytoscapeConverter {

  def convert(infilename: String, outfilename: String) = {
    val (nodes, edges) = new LinnaeusReader(infilename).read()

    new CytoscapeWriter(outfilename).write(nodes, edges)
  }
}
