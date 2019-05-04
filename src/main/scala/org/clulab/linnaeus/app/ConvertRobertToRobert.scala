package org.clulab.linnaeus.app

import org.clulab.linnaeus.model.converter.RobertToRobertConverter

/**
  * Use this to test the round trip through Reader and Writer.
  * Compare input to output.
  */
object ConvertRobertToRobert extends App {
  new RobertToRobertConverter("data/taxonomy_acquisition_data.tax").convert()
}
