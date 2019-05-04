package org.clulab.linnaeus.util

class First {
  protected var first = true

  def ifTrue(f: Unit => Unit): Unit = {
    if (first) f()
    if (first)
      first = false
  }

  def ifFalse(f: Unit => Unit): Unit = {
    if (!first) f()
    if (first)
      first = false
  }
}
