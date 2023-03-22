package project

import scala.swing.*

import javax.swing.ImageIcon
import project.Simulant

import scala.collection.parallel.immutable.ParSeq
import project.*
import project.config.*

import java.awt.{Color, Graphics2D, RenderingHints}
import java.awt.event.{ActionEvent, ActionListener}
import java.awt.geom.Ellipse2D
import scala.concurrent.*
import scala.concurrent.duration.Duration
import scala.swing.event.ButtonClicked



object SimGUI extends SimpleSwingApplication {
  private val areaWidth = 500
  private val areaHeight = 500
  private val settingsWidth = 250
  private val settingsHeight = 500

  var leader: Simulant = Simulant(true)
  var followers: ParSeq[Simulant] = ParSeq(Simulant(false), Simulant(false))
  private var simSpeed: Int = 1


  def top: Frame = new MainFrame:
    title = "Follow the leader sim"

    val areaPanel = new BoxPanel(Orientation.Vertical){
      preferredSize = new Dimension(areaWidth, areaHeight)
      background = java.awt.Color.GRAY

      override def paintComponent(pic: Graphics2D) =
        pic.fillRect(0, 0, areaWidth, areaHeight)
        pic.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON)
        pic.setColor(Color.RED)
        followers.foreach(i => pic.fill(Ellipse2D.Double(i.readPos._1, i.readPos._2, 10, 10)))
        pic.setColor(Color.BLUE)
        pic.fill(Ellipse2D.Double(leader.readPos._1, leader.readPos._2, 10, 10))
    }
    val settingsPanel = new BoxPanel(Orientation.Vertical):
      preferredSize = new Dimension(settingsWidth, settingsHeight)

      for i <- 1 to 9 do
        contents += new BoxPanel(Orientation.Horizontal):
          contents += new Label(s"Setting $i: ")
          contents += new TextField(10)
        contents += Swing.VStrut(5)
      val step =  new Button("One step")
      contents += step
      step.reactions += {
        case ButtonClicked(_) =>
          restart()
          areaPanel.repaint()}

    val splitPane = new SplitPane(Orientation.Vertical, settingsPanel, areaPanel):
      dividerLocation = settingsWidth

    contents = splitPane



  /** Changes the sim speed. Sim speed can be a integer from 1 to 10 */
  def changeSimSpeed(x: Int) =
    simSpeed = if x < 1 then 1 else if 10 < x then 10 else x

  def sysClock(): Unit =
    wait(100)

  def restart() =
    followers.foreach(_.restart())
    leader.restart()

  def pause() =
    wait(100)

  def update() =
  //  if isPaused then pause() else if config.restart then restart() else
   followers.foreach(_.update())
   leader.update()

}

