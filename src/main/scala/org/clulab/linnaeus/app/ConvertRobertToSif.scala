package org.clulab.linnaeus.app

import org.clulab.linnaeus.model.converter.RobertToSifConverter

/**
  * Load the resulting files into the Cytoscape application.
  */
object ConvertRobertToSif extends App {
//  new RobertToSifConverter("data/taxonomy_acquisition_data.tax").convert()
  new RobertToSifConverter("data/unprocessed_graph.tax").convert()
}
