package org.clulab.linnaeus.util

class Tabber protected (size: Int, useSpaces: Boolean) {

  def this() = this(1, false) // default to plain tabs

  def this(size: Int) = this(size, true) // substitute size spaces for tab

  protected val margin =
      if (useSpaces) " " * size
      else "\t"

  def tab(count: Int): String = margin * count

  def tab(count: Int, value: String): String = tab(count) + value
}
