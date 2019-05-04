package org.clulab.linnaeus.model.writer

import org.clulab.linnaeus.model.graph.eidos.EidosNetwork
import org.clulab.linnaeus.model.graph.robert.RobertNetwork

abstract class Writer {
  def write(network: EidosNetwork): Unit
  def write(network: RobertNetwork): Unit
}
