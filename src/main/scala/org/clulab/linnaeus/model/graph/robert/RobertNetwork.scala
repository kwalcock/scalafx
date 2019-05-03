package org.clulab.linnaeus.model.graph.robert

import org.clulab.linnaeus.model.graph.GraphNetwork
import org.clulab.linnaeus.model.graph.Indexer

class RobertNetwork(id: Int, path: String) extends GraphNetwork[
  Int,
  String, RobertNode,
  Int, RobertEdge
](id) {
  val edgeIndexer = new Indexer()

  /**
    * Networks in Robert format are not necessarily trees, so these methods need to be
    * provided for mkTree so that there can be a single root.
    */
  override def newRootNode(): RobertNode = new RobertNode(RobertNetwork.ROOT_NODE)

  override def newRootEdge() = new RobertEdge(edgeIndexer.next, None, RobertNetwork.ROOT_EDGE)
}

object RobertNetwork {
  val ROOT_NODE = "<root>"
  val ROOT_EDGE = "is_a_<root>_hypernym_of"
}
