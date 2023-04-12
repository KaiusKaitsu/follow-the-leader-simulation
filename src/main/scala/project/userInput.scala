package project

import project.Vector2D

val configS = configGeneric(50,50,50,50,10,(800,800),5)


class configGeneric(lSpeed: Int, fSpeed: Int, lMass: Int, fMass: Int, fNum: Int, map: (Int,Int), simS: Int):
  /** speed gives the amount of pixels that the simulant accelerates per clock cycle. it is a number from 1 to 100 , default being 50.
   * 1 "speed" accelerates 1 "mass" per cycle to speed of 1 pixel per cycle */
  private var leaderSpeed: Int = 1
  private var folowerSpeed: Int = 2
  /** determines the masses of the Simulants. Allowed mass is from 1 to 10, default 5 */
  private var leaderMass: Int = 3
  private var folowerMass: Int = 4
  /** Gives the amount of followers in the sim, default is 2 */
  private var followerNum: Int = 5
  /** map size gives the dimensions of the simulation area where the simulants live each side is limited from 100 to 500 pixels so largest map is 500x500 */
  private var MapSize: (Int, Int) = (800, 800)
  /** if isPaused is true the simulation will halt the update(). */
  var isPaused: Boolean = true
  /** Sim refresh rate in nanoseconds */
  private var simSpeed: Int = 6

  /** here are the methods to change and read the settings */
  def changeLeaderSpeed(x: Int): Unit =
    if x < 1 then leaderSpeed = 1 else if x > 100 then leaderSpeed = 100 else leaderSpeed = x

  def changeFollowerSpeed(x: Int): Unit =
    if x < 1 then folowerSpeed = 1 else if x > 100 then folowerSpeed = 100 else folowerSpeed = x

  def changeLeaderMass(x: Int): Unit =
    if x < 1 then leaderMass = 1 else if x > 100 then leaderMass = 100 else leaderMass = x

  def changeFollowerMass(x: Int): Unit =
    if x < 1 then folowerMass = 1 else if x > 100 then folowerMass = 100 else folowerMass = x

  def changeFollowerNum(x: Int): Unit =
    if x < 1 then followerNum = 1 else if x > 100 then followerNum = 100 else followerNum = x

  def changeSimSpeed(x: Int): Unit =
    if x < 1 then simSpeed = 1 else if x > 100 then simSpeed = 100 else simSpeed = x

  def readLeaderSpeed: Int = leaderSpeed

  def readFollowerSpeed: Int = folowerSpeed

  def readLeaderMass: Int = leaderMass

  def readFollowerMass: Int = folowerMass

  def readFollowerNum: Int = followerNum

  def readSimSpeed: Int = simSpeed

  def changeMapSize(x: Int, y: Int): Unit =
    val apuX: Int = if x < 100 then 100 else if x > 800 then 800 else x
    val apuY: Int = if y < 100 then 100 else if y > 800 then 800 else y
    MapSize = (apuX, apuY)

  def readMapSize: (Int, Int) = MapSize

  def fromSpecific(a: configGeneric) =
    changeLeaderSpeed(a.readLeaderSpeed)
    changeFollowerSpeed(a.readFollowerSpeed)
    changeLeaderMass(a.readLeaderMass)
    changeFollowerMass(a.readFollowerMass)
    changeFollowerNum(a.readFollowerNum)
    changeMapSize(a.readMapSize._1, a.readMapSize._2)
    changeSimSpeed(a.readSimSpeed)

  changeLeaderSpeed(lSpeed)
  changeFollowerSpeed(fSpeed)
  changeLeaderMass(lMass)
  changeFollowerMass(fMass)
  changeFollowerNum(fNum)
  changeMapSize(map._1, map._2)
  changeSimSpeed(simS)




