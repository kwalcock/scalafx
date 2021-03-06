package org.clulab.linnaeus.model.reader

import org.clulab.linnaeus.model.graph.robert.RobertEdge
import org.clulab.linnaeus.model.graph.robert.RobertNetwork
import org.clulab.linnaeus.model.graph.robert.RobertNode
import org.clulab.linnaeus.util.Closer.AutoCloser
import org.clulab.linnaeus.util.FileUtil

import scala.io.Source

class RobertReader(path: String) {

  def read(): RobertNetwork = {
    Source.fromFile(path, FileUtil.utf8).autoClose { source =>
      val network = new RobertNetwork(0, path)

      source.getLines().zipWithIndex.foreach { case (line, index) =>
        if (index == 0) {
          val nodes = line.split('\t').map { id => new RobertNode(id) }

          nodes.foreach(network.addNode)
        }
        else {
          val parts = line.split('\t')
          val sourceId = parts(0)
          val targetId = parts(1)
          val weightOpt = parts.lift(2).map(_.toDouble)
          val edge = new RobertEdge(network.edgeIndexer.next, weightOpt)

          network.addEdge(sourceId, edge, targetId)
        }
      }
      network
    }
  }
}

