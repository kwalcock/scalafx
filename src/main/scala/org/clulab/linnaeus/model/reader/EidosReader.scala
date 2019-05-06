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
    val yaml = new Yaml(new Constructor(classOf[JCollection[Any]]))
    val yamlNodes = FileUtil.newBufferedInputStream(path).autoClose { bufferedInputStream =>
      yaml.load(bufferedInputStream).asInstanceOf[JCollection[Any]].asScala
    }
    val network = new EidosNetwork(0, path)

    def yamlNodesToStrings(yamlNodes: mutable.Map[String, JCollection[Any]], name: String): Seq[String] =
      yamlNodes.get(name).map(_.asInstanceOf[JCollection[String]].asScala.toSeq).getOrElse(Seq.empty)

    // This code is largely stolen from Eidos
    def parseYamlLeaf(parentNodeOpt: Option[EidosNode], yamlNodes: mutable.Map[String, JCollection[Any]]): Unit = {
      val name = yamlNodes(EidosReader.NAME).asInstanceOf[String]
      val polarityOpt = yamlNodes.get(EidosReader.POLARITY).asInstanceOf[Option[Double]]
      val examples = yamlNodesToStrings(yamlNodes, EidosReader.EXAMPLES)
      val descriptions = yamlNodesToStrings(yamlNodes, EidosReader.DESCRIPTION)
      // These need to be valid regexes, but don't check that just yet.
      val patterns = yamlNodesToStrings(yamlNodes, EidosReader.PATTERN)
      val childNode = new EidosNode(network.nodeIndexer.next, name, polarityOpt, examples, descriptions, patterns)

      network.addNode(childNode)
      parentNodeOpt.foreach { parentNode =>
        val edge = new EidosEdge(network.edgeIndexer.next)

        network.addEdge(parentNode.getId, edge, childNode.getId)
      }
    }

    def parseYamlBranchOrLeaf(parentNodeOpt: Option[EidosNode], yamlNodes: Iterable[Any]): Unit = {
      yamlNodes.foreach { yamlNode =>
        if (yamlNode.isInstanceOf[String])
          throw new Exception(s"Ontology has string (${yamlNode.asInstanceOf[String]}) where it should have a map.")
        val map: mutable.Map[String, JCollection[Any]] = yamlNode.asInstanceOf[JMap[String, JCollection[Any]]].asScala
        val key: String = map.keys.head

        if (key == EidosReader.FIELD)
          parseYamlLeaf(parentNodeOpt, map)
        else {
          val childNode = new EidosNode(network.nodeIndexer.next, key)

          network.addNode(childNode)
          parentNodeOpt.foreach { parentNode =>
            val edge = new EidosEdge(network.edgeIndexer.next)

            network.addEdge(parentNode.getId, edge, childNode.getId)
          }
          parseYamlBranchOrLeaf(Some(childNode), map(key).asScala.toSeq)
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