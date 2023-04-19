package project

import scala.util.*
import project.configS

import project.SimGUI.*

/** this is the simulant class that holds the logic of the simulation. Simulants calculate their movement by themselfs.
 * A simulant can be either a leader or a follower. Differences between these two are: their physical values (mass, speed), target (followers follow the leader,
 * leaders target is either random or a mouse on the simulation area.) and collision between each other (followers can collide with the leader or each other but leader only collides with walls)
 */

class Simulant(isLeader: Boolean):


  /** Values of the simulant */
  private val rand = Random()
  private var position: Vector2D = Vector2D(rand.between(0,configS.readMapSize._1-10),rand.between(0,configS.readMapSize._2-10))
  private var momentum: Vector2D = Vector2D(0,0)
  private var leaderTarget = Vector2D(rand.between(0,configS.readMapSize._1),rand.between(0,configS.readMapSize._2))

  def mouseTarget(x: Int, y: Int) =
    leaderTarget = Vector2D(x,y)
  
  /** Target where the simulants goes.*/
  private def target: Vector2D = if !isLeader then SimGUI.leader.position else leaderTarget

  /** Mass of the simulant */
  private def mass: Int = if isLeader then configS.readLeaderMass else configS.readFollowerMass

  /** Speed of the simulant */
  private def speed: Double = if isLeader then configS.readLeaderSpeed/10.0 else configS.readFollowerSpeed/10.0

  /** Gives the x and y pos of the simulant */
  def readPos: (Int,Int) = (position.x.toInt, position.y.toInt)

  /** logic to enable kinetics between the simulants and also to make them unable to be all over each other. Note that only followers are affected */
  private def collisionCheck(newPosition: Vector2D, newMomentum: Vector2D) =
    val simulants = followers.filter(_ != this)
    for (sim <- simulants) do
      val distance = (newPosition - sim.position).magnitude
      if distance < 10 then //collision checker. Simulants radius is 5, when two of them are side by side the distance must be 10
        val normal = (newPosition - sim.position).normalized
        val tangent = Vector2D(-normal.y, normal.x)
        val thisNormalSpeed = normal.dot(newMomentum)
        val thisTangentSpeed = tangent.dot(newMomentum)
        val otherNormalSpeed = normal.dot(sim.momentum)
        val otherTangentSpeed = tangent.dot(sim.momentum)

        // Calculate new normal speeds after collision
        val newThisNormalSpeed = (thisNormalSpeed * (mass - sim.mass) + 2 * sim.mass * otherNormalSpeed) / (mass + sim.mass)
        val newOtherNormalSpeed = (otherNormalSpeed * (sim.mass - mass) + 2 * mass * thisNormalSpeed) / (mass + sim.mass)

        // Update momentum
        val newThisMomentum = (normal * newThisNormalSpeed) + (tangent * thisTangentSpeed)
        val newOtherMomentum = (normal * newOtherNormalSpeed) + (tangent * otherTangentSpeed)
        momentum = newThisMomentum
        sim.momentum = newOtherMomentum

        // Move simulants slightly apart so they are no longer touching
        val pushVector = normal * 0.5
        position = newPosition + pushVector
        sim.position = sim.position - pushVector

  /** Logic to make the simulant bounce away from the simulation boundaries. Applies to both leader and followers */
  private def keepInBounds(newPosition: Vector2D, newMomentum: Vector2D)=
    newPosition match
      case Vector2D(x, _) if x <= 0 =>
        momentum = Vector2D((newMomentum.x * (-1)), newMomentum.y) / 2
        position = Vector2D(0, newPosition.y)
      case Vector2D(x, _) if x >= configS.readMapSize._1-10 =>
        momentum = Vector2D((newMomentum.x * (-1)), newMomentum.y) / 2
        position = Vector2D(configS.readMapSize._1-10, newPosition.y)
      case Vector2D(_, y) if y <= 0 =>
        momentum = Vector2D(newMomentum.x,(newMomentum.y * (-1))) / 2
        position = Vector2D(newPosition.x, 0)
      case Vector2D(_, y) if y >= configS.readMapSize._2-10 =>
        momentum = Vector2D(newMomentum.x,(newMomentum.y * (-1))) / 2
        position = Vector2D(newPosition.x, configS.readMapSize._2-10)
      case _ =>
        momentum = newMomentum
        position = newPosition

  /** This is the method that is called from SimGUI to update the simulants position and momentum. */
  def update() =
    val directionToTarget = (target - position).normalized
    val accelerationToTarget = directionToTarget * (speed / mass)
    /** newMomentum basicly limits the maximum possible magnitude of newMomentum (hence momentum) while still allowing change in the momemtums direction, allowing smooth and controlled movements.
     * also when simulant is approaching target the maximum momentum decreases minimizing orbiting behaviour of the leader around its targets */
    val newMomentum = (momentum + accelerationToTarget) * (1.5+(target - position).magnitude/1000)/(momentum +accelerationToTarget).magnitude
    val newPosition = position + newMomentum
    if isLeader&&((this.position-target).magnitude<10) then leaderTarget=Vector2D(rand.between(0,configS.readMapSize._1),rand.between(0,configS.readMapSize._2))
    collisionCheck(newPosition,newMomentum)
    keepInBounds(newPosition,newMomentum)

  /** Randomizes the position of the simulant and zeroes the momentum. This is called from the SimGUI */
  def restart() =
    position = Vector2D(rand.between(0,configS.readMapSize._1-10),rand.between(0,configS.readMapSize._2-10))
    momentum = Vector2D(0,0)

/** this is the 2D vector physics that the simulants follow */
case class Vector2D(x: Double, y: Double):
  def +(that: Vector2D) = Vector2D(x + that.x, y + that.y)
  def -(that: Vector2D) = Vector2D(x - that.x, y - that.y)
  def *(scalar: Double) = Vector2D(x * scalar, y * scalar)
  def /(scalar: Double) = Vector2D(x / scalar, y / scalar)
  def magnitude = scala.math.sqrt(scala.math.pow(x, 2) + scala.math.pow(y, 2))
  def normalized = this / magnitude
  def dot(that: Vector2D): Double = x * that.x + y * that.y
