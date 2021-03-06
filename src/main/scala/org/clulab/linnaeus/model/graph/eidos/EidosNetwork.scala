package org.clulab.linnaeus.model.graph.eidos

import org.clulab.linnaeus.model.graph.GraphNetwork
import org.clulab.linnaeus.model.graph.Indexer

class EidosNetwork(id: Int, path: String) extends GraphNetwork[
  Int,
  Int, EidosNode,
  Int, EidosEdge
](id) {
  /**
    * Networks in Eidos format have nodes (and implied edges) that are distinct based on
    * their very existence in the hierarchy.  To assure this, they are all given unique IDs
    * that are generated by these indexers.
    */
  val nodeIndexer = new Indexer()
  val edgeIndexer = new Indexer()

  protected val indexer = new Indexer()
}
