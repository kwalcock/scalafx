package org.clulab.linnaeus.app

import org.clulab.linnaeus.model.EidosNode
import org.clulab.linnaeus.model.converter.EidosToSifConverter

object ConvertEidosToSif extends App {
  EidosToSifConverter.convert(EidosNode.UN, "un.sif")
}
