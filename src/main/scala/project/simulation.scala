/*package project

import project.Simulant
import scala.collection.parallel.immutable.ParSeq
import project.*
import project.config.*
import scala.concurrent.*
import scala.concurrent.duration.Duration
import project.simulationDraw
*/
/** Legacy code (or something) not used anymore. Simulation object and SimGUI were merged hence this has been left unused. Will be kept in the directory untill final version*/
/*
object simulation:


  var followers: ParSeq[Simulant] = ???
  var leader: Simulant = Simulant(true)
  var simSpeed: Int = 1

  var simDraw = simulationDraw(leader,followers)
  
  /** Changes the sim speed. Sim speed can be a integer from 1 to 10 */
  def changeSimSpeed(x: Int) =
    simSpeed =  if x<1 then 1 else if 10<x then 10 else x

  def sysClock(): Unit =
    wait(100)


  def restart() =
    followers.foreach(_.restart())
    leader.restart()
    simDraw.draw()

  def pause() =
    wait(100)

  def update() =
    if isPaused then pause() else if config.restart then restart() else
      followers.foreach(_.update())
      leader.update()
      simDraw.draw()

   /** Here the simulation loop will run */



end simulation

*/