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

  protected def random(levelsRemaining: Int, countRemaining: Int): (TreeItem[String], Int) = {
    val value = randomWord
    val treeItem = new TreeItem(value)
    treeItem.expanded = true
    var countUsed = 1
    val length = rng.nextInt(10) + 1

    0.until(length).foreach { index =>
      if (0 < levelsRemaining && countUsed < countRemaining) {
        val (child, used) = random(levelsRemaining - 1, countRemaining - countUsed)

        treeItem.children.add(child)
        countUsed += used
      }
    }
    (treeItem, countUsed)
  }

  def random: TreeItem[String] = {
    var (treeItem, countUsed) = random(levelsRemaining = 10, countRemaining = 100000)
    println("Count used: " + countUsed)
    treeItem
  }
}
