package org.clulab.linnaeus.app

import org.clulab.linnaeus.model.converter.LinnaeusToGephiConverter

object ConvertLinnaeusToGephi extends App {
  LinnaeusToGephiConverter.convert("taxonomy_acquisition_data", "taxonomy")
}
