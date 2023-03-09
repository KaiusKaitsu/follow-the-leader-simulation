package project

import scala.swing.*
import project.simulationDraw
import javax.swing.ImageIcon
import project.Simulant
import scala.collection.parallel.immutable.ParSeq
import project.*
import project.config.*
import scala.concurrent.*
import scala.concurrent.duration.Duration



object SimGUI extends SimpleSwingApplication {
  private val areaWidth = 500
  private val areaHeight = 500
  private val settingsWidth = 250
  private val settingsHeight = 500

  def top: Frame = new MainFrame:
    title = "Follow the leader sim"

    val areaPanel = new BoxPanel(Orientation.Vertical):
      preferredSize = new Dimension(areaWidth, areaHeight)
      background = java.awt.Color.GRAY
      contents += new Label {
        icon = new ImageIcon(simulationDraw(leader,followers).test)
        horizontalAlignment = Alignment.Center
        verticalAlignment = Alignment.Center
      }
      xLayoutAlignment = 0.5f
      yLayoutAlignment = 0.5f

    val settingsPanel = new BoxPanel(Orientation.Vertical):
      preferredSize = new Dimension(settingsWidth, settingsHeight)

      for i <- 1 to 10 do
        contents += new BoxPanel(Orientation.Horizontal):
          contents += new Label(s"Setting $i: ")
          contents += new TextField(10)
        contents += Swing.VStrut(5)

    val splitPane = new SplitPane(Orientation.Vertical, settingsPanel, areaPanel):
      dividerLocation = settingsWidth

    contents = splitPane

  /** following has been migrated from simulation object that was merged with SimGUI */

  var leader: Simulant = Simulant(true)
  var followers: ParSeq[Simulant] = ParSeq(Simulant(false),Simulant(false))
  var simSpeed: Int = 1

  var simDraw = simulationDraw(leader, followers)

  /** Changes the sim speed. Sim speed can be a integer from 1 to 10 */
  def changeSimSpeed(x: Int) =
    simSpeed = if x < 1 then 1 else if 10 < x then 10 else x

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

}

