package org.clulab.linnaeus.model.converter

import org.clulab.linnaeus.StringUtil

abstract class Converter(val inFilename: String) {

  def convert(): Unit

  def baseFilename: String = {
    val filename = StringUtil.afterLast(inFilename, '/', true)
    val basename = StringUtil.beforeLast(filename, '.', true)

    basename
  }
}
