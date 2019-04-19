package org.clulab.linnaeus.app

import org.clulab.linnaeus.model.converter.EidosToSifConverter

object ConvertEidosToSif extends App {
  EidosToSifConverter.convert("un.sif")
}
