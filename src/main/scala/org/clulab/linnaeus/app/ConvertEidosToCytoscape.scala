package org.clulab.linnaeus.app

import org.clulab.linnaeus.model.converter.EidosToCytoscapeConverter

/**
  * The resulting file is referenced from a web page for presentation
  * in the browser.  See src/main/html/cytoscape.
  */
object ConvertEidosToCytoscape extends App {
  new EidosToCytoscapeConverter("data/un_ontology.yml").convert()
}
