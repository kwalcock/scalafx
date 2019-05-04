package org.clulab.linnaeus.model.graph.robert

import org.clulab.linnaeus.model.graph.GraphEdge

class RobertEdge(id: Int, val weightOpt: Option[Double], val relation: String = RobertEdge.RELATION)
    extends GraphEdge[Int](id) {
}

object RobertEdge {
  val RELATION: String = "is_a_hypernym_of"
}
