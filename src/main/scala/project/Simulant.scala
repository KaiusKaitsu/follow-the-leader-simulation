package project

import scala.util.*
import project.config.*
import project.SimGUI


class Simulant(isLeader: Boolean):

  private val rand = Random()
  private var position: Vector2D = Vector2D(rand.between(0,readMapSize._1),rand.between(0,readMapSize._2))
  private var momentum: Vector2D = Vector2D(0,0)
  private var maxDistance = 20
  
  private def target: Simulant = if !isLeader then SimGUI.leader else this
  
  private def mass: Int = if isLeader then readLeaderMass else readFollowerMass

  
  def readPos: (Int,Int) = (position.x.toInt,position.y.toInt)

  def update() =
    val distanceToTarget = (target.position - position).magnitude
    val directionToTarget = (target.position - position).normalized
    val distanceToMove = math.min(maxDistance, distanceToTarget)
    val accelerationToTarget = directionToTarget * (distanceToMove / mass)
    val newMomentum = momentum + accelerationToTarget
    val newPosition = position + newMomentum
    momentum = newMomentum
    position = newPosition
  
  def restart() =
    position = Vector2D(rand.between(0,readMapSize._1),rand.between(0,readMapSize._2))
    momentum = Vector2D(0,0)

/** this is the 2D physics that the simulants and possible other entities follow */
case class Vector2D(x: Double, y: Double):
  def +(that: Vector2D) = Vector2D(x + that.x, y + that.y)
  def -(that: Vector2D) = Vector2D(x - that.x, y - that.y)
  def *(scalar: Double) = Vector2D(x * scalar, y * scalar)
  def /(scalar: Double) = Vector2D(x / scalar, y / scalar)
  def magnitude = scala.math.sqrt(scala.math.pow(x, 2) + scala.math.pow(y, 2))
  def normalized = this / magnitude


