package org.clulab.linnaeus.util

import java.io.BufferedInputStream
import java.io.BufferedOutputStream
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.OutputStreamWriter
import java.io.PrintWriter
import java.nio.charset.StandardCharsets

object FileUtil {
  val utf8: String = StandardCharsets.UTF_8.toString

  def newPrintWriter(filename: String): PrintWriter = {
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
  }

  def newBufferedInputStream(file: File): BufferedInputStream =
    new BufferedInputStream(new FileInputStream(file))

  def newBufferedInputStream(filename: String): BufferedInputStream =
    newBufferedInputStream(new File(filename))
}
