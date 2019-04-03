package org.clulab.linnaeus.model.writer

import java.io.BufferedOutputStream
import java.io.File
import java.io.FileOutputStream
import java.io.OutputStreamWriter
import java.io.PrintWriter
import java.nio.charset.StandardCharsets

import org.clulab.linnaeus.model.OntologyTreeItem

class GephiWriter(val fileBasename: String) {

  protected def newPrintWriter(filename: String) =
      new PrintWriter(
        new OutputStreamWriter(
          new BufferedOutputStream(
            new FileOutputStream(
              new File(filename)
            )
          ),
          StandardCharsets.UTF_8.toString
        )
      )

  protected def writeNode(item: OntologyTreeItem, printWriter: PrintWriter): Unit = {
    printWriter.print(item.id)
    printWriter.print(",")
    // Hack for CSV file
    printWriter.println(item.toString.replace(',', ';'))

    item.children.foreach(writeNode(_, printWriter))
  }

  protected def writeNode(root: OntologyTreeItem): Unit = {
    val filename = fileBasename + "_nodes.csv"
    val printWriter = new PrintWriter(filename)
    printWriter.println("id,label")
    writeNode(root, printWriter)
    printWriter.close()
  }

  protected def writeEdge(item: OntologyTreeItem, printWriter: PrintWriter): Unit = {
    item.children.foreach { child =>
      printWriter.print(child.id)
      printWriter.print(",")
      printWriter.println(item.id)
      writeEdge(child, printWriter)
    }
  }

  protected def writeEdge(root: OntologyTreeItem): Unit = {
    val filename = fileBasename + "_edges.csv"
    val printWriter = new PrintWriter(filename)
    printWriter.println("Source,Target")
    writeEdge(root, printWriter)
    printWriter.close()
  }

  def write(root: OntologyTreeItem): Unit = {
    writeNode(root)
    writeEdge(root)
  }
}
