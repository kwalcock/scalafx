package org.clulab.linnaeus.model.reader

import org.clulab.linnaeus.model.SimpleEdge
import org.clulab.linnaeus.model.SimpleNode
import org.clulab.linnaeus.util.Closer.AutoCloser
import org.clulab.linnaeus.util.FileUtil

import scala.io.Source

class LinnaeusReader(val filename: String) {

  def read(): (Seq[SimpleNode], Seq[SimpleEdge]) = {
    Source.fromFile(filename, FileUtil.utf8).autoClose { source =>
      var nodes: Seq[SimpleNode] = Seq.empty
      var edges: Seq[SimpleEdge] = Seq.empty

      source.getLines().zipWithIndex.foreach { case (line, index) =>
        if (index == 0)
          nodes = line.split('\t').map(SimpleNode)
        else {
          val Array(sourceId, targetId, _) = line.split('\t')
          val edgeId = sourceId + "_" + targetId

          edges = SimpleEdge(edgeId, sourceId, targetId) +: edges
        }
      }
      (nodes, edges.reverse)
    }
  }
}
