package org.clulab.linnaeus.model.converter

import org.clulab.linnaeus.model.LinnaeusNode
import org.clulab.linnaeus.model.reader.LinnaeusReader
import org.clulab.linnaeus.model.writer.D3Writer

object LinnaeusToD3Converter {

  def convert(infilename: String, outfilename: String) = {
    val (simpleNodes, simpleEdges) = new LinnaeusReader(infilename).read()
    val linnaeusNodes = simpleNodes.map { simpleNode => new LinnaeusNode(simpleNode.id) }
    val linnaeusNodeMap = linnaeusNodes.map { linnaeusNode => (linnaeusNode.id, linnaeusNode) }.toMap

    simpleEdges.foreach { simpleEdge =>
      val hypernym = linnaeusNodeMap.get(simpleEdge.sourceId)
      val hyponym = linnaeusNodeMap.get(simpleEdge.targetId)

      require(hypernym.isDefined)
      require(hyponym.isDefined)
      if (hyponym.get.parent.isEmpty)
        hypernym.get.addChild(hyponym.get)
      else
        println("Already has parent " + hyponym.get)
    }

    val roots = linnaeusNodes.filter { linnaeusNode => linnaeusNode.parent.isEmpty }
    val root = if (roots.size == 1)
      roots.head
    else {
      val newRoot = new LinnaeusNode("<root>")

      roots.foreach { oldRoot => newRoot.addChild(oldRoot) }
      newRoot
    }
    new D3Writer(outfilename).write(root)
  }
}
