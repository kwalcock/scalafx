package org.clulab.linnaeus.app

import org.clulab.linnaeus.model.converter.LinnaeusToSifConverter

object ConvertLinnaeusToSif extends App {
//  LinnaeusToSifConverter.convert("taxonomy_acquisition_data", "taxonomy.sif", true)
  LinnaeusToSifConverter.convert("unprocessed_graph", "unprocessed.sif", false)
}
