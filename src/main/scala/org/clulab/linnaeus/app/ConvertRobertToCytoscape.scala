package org.clulab.linnaeus.app

import org.clulab.linnaeus.model.converter.RobertToCytoscapeConverter

/**
  * The resulting file is referenced from a web page for presentation
  * in the browser.  See src/main/html/cytoscape.
  */
object ConvertRobertToCytoscape extends App {
  new RobertToCytoscapeConverter("data/taxonomy_acquisition_data.tax").convert()
}
