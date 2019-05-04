package org.clulab.linnaeus.app

import org.clulab.linnaeus.model.converter.EidosToD3Converter

/**
  * The resulting file is referenced from a web page for presentation
  * in the browser.  See src/main/html/d3.
  */
object ConvertEidosToD3 extends App {
  new EidosToD3Converter("data/un_ontology.yml").convert()
}
