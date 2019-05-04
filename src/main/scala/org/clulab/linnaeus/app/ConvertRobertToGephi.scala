package org.clulab.linnaeus.app

import org.clulab.linnaeus.model.converter.RobertToGephiConverter

/**
  * Load the resulting files into the Gephi application.
  */
object ConvertRobertToGephi extends App {
  new RobertToGephiConverter("data/taxonomy_acquisition_data.tax").convert()
}
