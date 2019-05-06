package org.clulab.linnaeus.model.reader

import java.util.{Collection => JCollection}
import java.util.{Map => JMap}

import org.clulab.linnaeus.model.graph.eidos.EidosEdge
import org.clulab.linnaeus.model.graph.eidos.EidosNetwork
import org.clulab.linnaeus.model.graph.eidos.EidosNode
import org.clulab.linnaeus.util.Closer.AutoCloser
import org.clulab.linnaeus.util.FileUtil
import org.yaml.snakeyaml.Yaml
import org.yaml.snakeyaml.constructor.Constructor

import scala.collection.JavaConverters._
import scala.collection.mutable
import scala.io.Source

class EidosReader(path: String) {

  def read(): EidosNetwork = {
    val yamlText = Source.fromFile(path, FileUtil.utf8).autoClose { source =>
      source.mkString
    }
    val yaml = new Yaml(new Constructor(classOf[JCollection[Any]]))
    // This can also be done with an input stream, which would be better for large files.
    val yamlNodes = yaml.load(yamlText).asInstanceOf[JCollection[Any]].asScala
    val network = new EidosNetwork(0, path)

    def yamlNodesToStrings(yamlNodes: mutable.Map[String, JCollection[Any]], name: String): Seq[String] =
      yamlNodes.get(name).map(_.asInstanceOf[JCollection[String]].asScala.toSeq).getOrElse(Seq.empty)

    // This code is largely stolen from Eidos
    def parseYamlLeaf(parentPacketOpt: Option[network.NodePacket], yamlNodes: mutable.Map[String, JCollection[Any]]): Unit = {
      val name = yamlNodes(EidosReader.NAME).asInstanceOf[String]
      val polarityOpt = yamlNodes.get(EidosReader.POLARITY).asInstanceOf[Option[Double]]
      val examples = yamlNodesToStrings(yamlNodes, EidosReader.EXAMPLES)
      val descriptions = yamlNodesToStrings(yamlNodes, EidosReader.DESCRIPTION)
      // These need to be valid regexes, but don't check that just yet.
      val patterns = yamlNodesToStrings(yamlNodes, EidosReader.PATTERN)
      val childPacket: network.NodePacket = network.newNodePacket(new EidosNode(network.nodeIndexer.next, name, polarityOpt, examples, descriptions, patterns))

      parentPacketOpt.foreach { parentPacket =>
        val edge = new EidosEdge(network.edgeIndexer.next)

        network.newEdge(parentPacket, edge, childPacket)
      }
    }

    def parseYamlBranchOrLeaf(parentPacketOpt: Option[network.NodePacket], yamlNodes: Iterable[Any]): Unit = {
      yamlNodes.foreach { yamlNode =>
        if (yamlNode.isInstanceOf[String])
          throw new Exception(s"Ontology has string (${yamlNode.asInstanceOf[String]}) where it should have a map.")
        val map: mutable.Map[String, JCollection[Any]] = yamlNode.asInstanceOf[JMap[String, JCollection[Any]]].asScala
        val key: String = map.keys.head

        if (key == EidosReader.FIELD)
          parseYamlLeaf(parentPacketOpt, map)
        else {
          val childPacket = network.newNodePacket(new EidosNode(network.nodeIndexer.next, key))

          parentPacketOpt.foreach { parentPacket =>
            val edge = new EidosEdge(network.edgeIndexer.next)

            network.newEdge(parentPacket, edge, childPacket)
          }
          parseYamlBranchOrLeaf(Some(childPacket), map(key).asScala.toSeq)
        }
      }
    }

    parseYamlBranchOrLeaf(None, yamlNodes)
    network
  }
}

object EidosReader {
  val FIELD = "OntologyNode"
  val NAME = "name"
  val EXAMPLES = "examples"
  val DESCRIPTION = "descriptions"
  val POLARITY = "polarity"
  val PATTERN = "pattern"
}