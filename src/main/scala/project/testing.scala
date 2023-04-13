package project

import project.*
import scala.swing.*
import java.awt.image.BufferedImage
import javax.swing.ImageIcon
import project.Simulant
import scala.collection.parallel.immutable.ParSeq
import project.FileReader

/** TestImage is used to test created and external images to determine how well simulationDraw works*/

/*
object Vector2DTest extends App
  val a = Vector2D(1.0, 2.0)
  val b = Vector2D(3.0, 4.0)

  // Test vector addition
  val c = a + b
  assert(c.x == 4.0 && c.y == 6.0)

  // Test vector subtraction
  val d = b - a
  assert(d.x == 2.0 && d.y == 2.0)

  // Test scalar multiplication
  val e = a * 2.0
  assert(e.x == 2.0 && e.y == 4.0)

  // Test scalar division
  val f = b / 2.0
  assert(f.x == 1.5 && f.y == 2.0)

  // Test vector magnitude
  val g = Vector2D(3.0, 0.0)
  assert(g.magnitude == 3.0)

  // Test vector normalization
  val h = Vector2D(1.0, 1.0)
  val normalizedH = h.normalized
  assert(normalizedH.x == 1 / scala.math.sqrt(2.0) && normalizedH.y == 1 / scala.math.sqrt(2.0))

/** Test for simlationDraw */
*/

object quicky extends App:
  val x = FileReader.getConfig("81 76 49 60 10 5")
  if x.isDefined then println("dad") else println("död")
  println(x.get.readLeaderSpeed)
  println(x.get.readFollowerSpeed)
  println(x.get.readLeaderMass)
  println(x.get.readFollowerMass)
  println(x.get.readFollowerNum)
  println(x.get.readMapSize)
  println(x.get.readSimSpeed)
  sys.exit(0)