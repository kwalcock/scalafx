package org.clulab.linnaeus.app

import org.clulab.linnaeus.model.converter.EidosToGephiConverter

/**
  * Load the resulting files into the Gephi application.
  */
object ConvertEidosToGephi extends App {
  new EidosToGephiConverter("data/un_ontology.yml").convert()
}
