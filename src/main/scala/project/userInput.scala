package project


object config:
/** speed gives the amount of pixels that the simulant moves per clock cycle. it is a number from 1 to 10 , default being 5 */
  private var leaderSpeed: Int = 5
  private var folowerSpeed: Int = 5
/** map size gives the dimensions of the simulation area where the simulants live each side is limited from 100 to 500 pixels so largest map is 500x500 */
  private var MapSize: (Int, Int) = (500,500)
/** if isPaused is true the simulation will halt the update() if restart is pressed the simulation is paused and randomized to start.*/
  var isPaused: Boolean = true
  var restart: Boolean = false

/** here are the methods to change and read the settings*/
  def changeLeaderSpeed(x:Int): Unit =
    if x<1 then leaderSpeed = 1 else if x>10 then leaderSpeed = 10 else leaderSpeed = x
  def changeFollowerSpeed(x: Int): Unit =
      if x < 1 then folowerSpeed = 1 else if x > 10 then folowerSpeed = 10 else folowerSpeed = x
  def readLeaderSpeed: Int = leaderSpeed
  def readFollowerSpeed: Int = folowerSpeed

  def changeMapSize(x: Int, y: Int): Unit=
    val apuX: Int = if x<100 then 100 else if x>500 then 500 else x
    val apuY: Int = if y<100 then 100 else if y>500 then 500 else y
    MapSize = (apuX,apuY)
  def readMapSize: (Int,Int) = MapSize
  