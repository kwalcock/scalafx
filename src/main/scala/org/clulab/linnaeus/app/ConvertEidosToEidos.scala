package org.clulab.linnaeus.app

import org.clulab.linnaeus.model.converter.EidosToEidosConverter

/**
  * Use this to test the round trip through Reader and Writer.
  * Compare input to output.
  */
object ConvertEidosToEidos extends App {
  new EidosToEidosConverter("data/un_ontology.yml").convert()
}
