package org.clulab.linnaeus.app

import org.clulab.linnaeus.model.converter.EidosToD3Converter

object ConvertEidosToD3 extends App {
  EidosToD3Converter.convert("un.json")
}
