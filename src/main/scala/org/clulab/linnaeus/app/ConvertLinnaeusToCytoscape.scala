package org.clulab.linnaeus.app

import org.clulab.linnaeus.model.converter.LinnaeusToCytoscapeConverter

object ConvertLinnaeusToCytoscape extends App {
  LinnaeusToCytoscapeConverter.convert("taxonomy_acquisition_data", "elements.js")
}
