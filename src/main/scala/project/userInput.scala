package project

import project.Vector2D
import play.api.libs.json.*
import play.api.libs.functional.syntax._

/** this is a instance of the class configGeneric and its the only one that is used in the simulation.  */

val configS = configGeneric(50,50,50,50,10,(700,700),5)

/** Class configGeneric is how the data in the program is saved. ConfigGeneric is class because it needs to be one for the playframeworks json tools.
 * It was previously a object but it did not work with json tools. This class is what is used to read and write and change the settigns of the program from the GUI */

class configGeneric(var leaderSpeed: Int, var folowerSpeed: Int, var leaderMass: Int, var folowerMass: Int, var followerNum: Int, var MapSize: (Int,Int), var simSpeed: Int):
  
  var isPaused: Boolean = true

/** format is for the json tools to implicitly tell how to write and parse these values to json format. */

  implicit val format: Format[configGeneric] = new Format[configGeneric]:
    def reads(json: JsValue): JsResult[configGeneric] = 
      val leaderSpeed = (json \ "leaderSpeed").as[Int]
      val followerSpeed = (json \ "followerSpeed").as[Int]
      val leaderMass = (json \ "leaderMass").as[Int]
      val followerMass = (json \ "followerMass").as[Int]
      val followerNum = (json \ "followerNum").as[Int]
      val mapSize = (json \ "MapSize").as[(Int, Int)]
      val simSpeed = (json \ "simSpeed").as[Int]
      JsSuccess(new configGeneric(leaderSpeed, followerSpeed, leaderMass, followerMass, followerNum, mapSize, simSpeed))
    

    def writes(config: configGeneric): JsValue = Json.obj(
      "leaderSpeed" -> config.leaderSpeed,
      "followerSpeed" -> config.folowerSpeed,
      "leaderMass" -> config.leaderMass,
      "followerMass" -> config.folowerMass,
      "followerNum" -> config.followerNum,
      "MapSize" -> config.MapSize,
      "simSpeed" -> config.simSpeed)
  

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

  /** since only configS instance is used in the simulation this is how it can be changed easily.  */
  
  def fromSpecific(a: configGeneric) =
    changeLeaderSpeed(a.readLeaderSpeed)
    changeFollowerSpeed(a.readFollowerSpeed)
    changeLeaderMass(a.readLeaderMass)
    changeFollowerMass(a.readFollowerMass)
    changeFollowerNum(a.readFollowerNum)
    changeSimSpeed(a.readSimSpeed)

  /** when a new configGeneric is created these lines make sure that the given values are valid. */
  
  changeLeaderSpeed(readLeaderSpeed)
  changeFollowerSpeed(readFollowerSpeed)
  changeLeaderMass(readLeaderMass)
  changeFollowerMass(readFollowerMass)
  changeFollowerNum(readFollowerNum)
  changeSimSpeed(readSimSpeed)



