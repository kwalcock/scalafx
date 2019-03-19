package org.clulab.linnaeus.model

import scala.util.Random

case class TableNode(hypernym: String, hyponym: String, example: String, priority: Int)

object TableNode {
  protected val rng = new Random()

  protected def randomWord(): String = {
    val chars = 'a' to 'z'
    val length = rng.nextInt(10) + 1
    val stringBuilder = new StringBuilder

    0.until(length).foreach { _ => stringBuilder.append(chars(rng.nextInt(chars.length))) }
    stringBuilder.toString
  }

  def random: TableNode = {
    val hypernym = randomWord
    val hyponym = randomWord
    val length = rng.nextInt(10) + 1
    val words = 0.to(length).map(_ => randomWord)
    val example = words.mkString(" ")
    val priority = rng.nextInt(99) + 1

    TableNode(hypernym, hyponym, example, priority)
  }
}
