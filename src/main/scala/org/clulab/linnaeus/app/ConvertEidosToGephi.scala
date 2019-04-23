package org.clulab.linnaeus.app

import org.clulab.linnaeus.model.EidosNode
import org.clulab.linnaeus.model.converter.EidosToGephiConverter

object ConvertEidosToGephi extends App {
  EidosToGephiConverter.convert(EidosNode.UN, "un")
}
