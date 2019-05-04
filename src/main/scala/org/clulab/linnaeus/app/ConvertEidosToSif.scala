package org.clulab.linnaeus.app

import org.clulab.linnaeus.model.converter.EidosToSifConverter

/**
  * Load the resulting files into the Gephi application.
  */
object ConvertEidosToSif extends App {
  new EidosToSifConverter("data/un_ontology.yml").convert()
}
