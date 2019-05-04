package org.clulab.linnaeus.app

import org.clulab.linnaeus.model.converter.RobertToD3Converter

/**
  * The resulting file is referenced from a web page for presentation
  * in the browser.  See src/main/html/d3.
  */
object ConvertRobertToD3 extends App {
  new RobertToD3Converter("data/taxonomy_acquisition_data.tax").convert()
}
