package org.clulab.linnaeus.app

import org.clulab.linnaeus.model.converter.EidosToGephiConverter

object ConvertEidosToGephi extends App {
  EidosToGephiConverter.convert("test")
}
