package org.clulab.linnaeus.model.reader

import org.clulab.linnaeus.model.SimpleEdge
import org.clulab.linnaeus.model.SimpleNode
import org.clulab.linnaeus.util.Closer.AutoCloser
import org.clulab.linnaeus.util.FileUtil

import scala.io.Source

class LinnaeusReader(val filename: String, val hasWeights: Boolean = true) {

  def read(): (Seq[SimpleNode], Seq[SimpleEdge]) = {
    Source.fromFile(filename, FileUtil.utf8).autoClose { source =>
      var nodes: Seq[SimpleNode] = Seq.empty
      var edges: Seq[SimpleEdge] = Seq.empty

      source.getLines().zipWithIndex.foreach { case (line, index) =>
        if (index == 0)
          nodes = line.split('\t').map(SimpleNode)
        else {
          val (sourceId, targetId) = if (hasWeights) {
            val Array(sourceId, targetId, _) = line.split('\t')
            (sourceId, targetId)
          }
          else {
            val Array(sourceId, targetId) = line.split('\t')
            (sourceId, targetId)
          }
          val edgeId = sourceId + "_" + targetId

          edges = SimpleEdge(edgeId, sourceId, targetId) +: edges
        }
      }
      (nodes, edges.reverse)
    }
  }
}
