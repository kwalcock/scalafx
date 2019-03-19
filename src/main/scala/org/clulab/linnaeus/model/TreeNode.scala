package org.clulab.linnaeus.model

import scalafx.scene.control.TreeItem

import scala.util.Random

object TreeNode {
  protected val rng = new Random()

  protected def randomWord(): String = {
    val chars = 'a' to 'z'
    val length = rng.nextInt(10) + 1
    val stringBuilder = new StringBuilder

    0.until(length).foreach { _ => stringBuilder.append(chars(rng.nextInt(chars.length))) }
    stringBuilder.toString
  }

  protected def innerRandom(levelsRemaining: Int, countRemaining: Int): (TreeItem[String], Int) = {
    val value = randomWord
    val treeItem = new TreeItem(value)
    treeItem.expanded = true
    var countUsed = 1
    val length = rng.nextInt(10) + 1

    0.until(length).foreach { index =>
      if (0 < levelsRemaining && countUsed < countRemaining) {
        val (child, used) = innerRandom(levelsRemaining - 1, countRemaining - countUsed)

        treeItem.children.add(child)
        countUsed += used
      }
    }
    (treeItem, countUsed)
  }

  def random(levels: Int, count: Int): TreeItem[String] = {
    var (treeItem, countUsed) = innerRandom(levels, count)
    println("Count used: " + countUsed)
    treeItem
  }
}
