package project

import scala.util.*
import project.config.*
import project.SimGUI.*


class Simulant(isLeader: Boolean):

  /** Values of the simulant */
  private val rand = Random()
  private var position: Vector2D = Vector2D(rand.between(0,readMapSize._1),rand.between(0,readMapSize._2))
  private var momentum: Vector2D = Vector2D(0,0)
  private var safeSpace = 5
  private var leaderTarget = Vector2D(rand.between(0,readMapSize._1),rand.between(0,readMapSize._2))

  /** Target where the simulants goes */
  private def target: Vector2D = if !isLeader then SimGUI.leader.position else leaderTarget

  /** Mass of the simulant */
  private def mass: Int = if isLeader then readLeaderMass else readFollowerMass

  /** Mass of the simulant */
  private def speed: Double = if isLeader then readLeaderSpeed/10.0 else readFollowerSpeed/10.0

  /** Gives the x and y pos of the simulant */
  def readPos: (Int,Int) = (position.x.toInt, position.y.toInt)

  /** Logic to make the simulant bounce away from the simulation boundaries. */
  private def keepInBounds(newPosition: Vector2D, newMomentum: Vector2D)=
    newPosition match
      case Vector2D(x, _) if x <= 0 =>
        momentum = Vector2D((newMomentum.x * (-1)), newMomentum.y) / 2
        position = Vector2D(0, newPosition.y)
      case Vector2D(x, _) if x >= readMapSize._1-10 =>
        momentum = Vector2D((newMomentum.x * (-1)), newMomentum.y) / 2
        position = Vector2D(readMapSize._1-10, newPosition.y)
      case Vector2D(_, y) if y <= 0 =>
        momentum = Vector2D(newMomentum.x,(newMomentum.y * (-1))) / 2
        position = Vector2D(newPosition.x, 0)
      case Vector2D(_, y) if y >= readMapSize._2-10 =>
        momentum = Vector2D(newMomentum.x,(newMomentum.y * (-1))) / 2
        position = Vector2D(newPosition.x, readMapSize._2-10)
      case _ =>
        momentum = newMomentum
        position = newPosition

  /** This is the method that is called from SimGUI to update the simulants position and momentum. */
  def update() =
    val directionToTarget = (target - position).normalized
    val accelerationToTarget = directionToTarget * (speed / mass)
    /** newMomentum implemented as follows: Earlier versions have been quite chaotic so to make the simulation smoother an additional brake has been added.
     * it basicly limits the maximum possible magnitude of newMomentum (hence momentum) while still allowing change in the momemtums direction, allowing smooth and controlled movements.
     * also when simulant is approaching target the maximum momentum decreases minimizing orbiting behaviour of the leader around its targets */
    val newMomentum = (momentum + accelerationToTarget) * (1.5+(target - position).magnitude/1000)/(momentum +accelerationToTarget).magnitude
    val newPosition = position + newMomentum
    if isLeader&&((this.position-target).magnitude<10) then leaderTarget=Vector2D(rand.between(0,readMapSize._1),rand.between(0,readMapSize._2))
    //collisionCheck(newPosition,newMomentum)
    keepInBounds(newPosition,newMomentum)

  /** Randomizes the position of the simulant and zeroes the momentum. This is called from the SimGUI */
  def restart() =
    position = Vector2D(rand.between(0,readMapSize._1),rand.between(0,readMapSize._2))
    momentum = Vector2D(0,0)

/** this is the 2D physics that the simulants follow */
case class Vector2D(x: Double, y: Double):
  def +(that: Vector2D) = Vector2D(x + that.x, y + that.y)
  def -(that: Vector2D) = Vector2D(x - that.x, y - that.y)
  def *(scalar: Double) = Vector2D(x * scalar, y * scalar)
  def /(scalar: Double) = Vector2D(x / scalar, y / scalar)
  def magnitude = scala.math.sqrt(scala.math.pow(x, 2) + scala.math.pow(y, 2))
  def normalized = this / magnitude
  def dot(that: Vector2D): Double = x * that.x + y * that.y
