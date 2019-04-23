package org.clulab.linnaeus.model.reader

import org.clulab.linnaeus.model.LinnaeusNode
import org.clulab.linnaeus.util.Closer.AutoCloser
import org.clulab.linnaeus.util.FileUtil

import scala.io.Source

class LinnaeusReader(val filename: String, val hasWeights: Boolean = true) {

  def read(): Seq[LinnaeusNode.Node] = {
    Source.fromFile(filename, FileUtil.utf8).autoClose { source =>
      var nodes: Seq[LinnaeusNode.Node] = Seq.empty
      var nodeMap: Map[String, LinnaeusNode.Node] = Map.empty

      source.getLines().zipWithIndex.foreach { case (line, index) =>
        if (index == 0) {
          nodes = line.split('\t').map { nodeId => new LinnaeusNode.Node(nodeId) }
          nodes.foreach { node => nodeMap = nodeMap + (node.value -> node ) }
        }
        else {
          val (sourceId, targetId) = if (hasWeights) {
            val Array(sourceId, targetId, _) = line.split('\t')
            (sourceId, targetId)
          }
          else {
            val Array(sourceId, targetId) = line.split('\t')
            (sourceId, targetId)
          }
          val parent = nodeMap(sourceId)
          val child = nodeMap(targetId)

          parent.addChild(child)
        }
      }

      val roots = nodes.filter { node => node.isRoot }
      roots
    }
  }
}
