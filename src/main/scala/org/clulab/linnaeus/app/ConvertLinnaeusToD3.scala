package org.clulab.linnaeus.app

import org.clulab.linnaeus.model.converter.LinnaeusToD3Converter

object ConvertLinnaeusToD3 extends App {
  LinnaeusToD3Converter.convert("taxonomy_acquisition_data", "taxonomy.json")
}
