package org.clulab.linnaeus.model.graph.eidos

import org.clulab.linnaeus.model.graph.GraphEdge

class EidosEdge(id: Int, val relation: String = EidosEdge.RELATION)
    extends GraphEdge[Int](id)  {
}

object EidosEdge {
  val RELATION: String = "is_the_parent_of"
}
