package project

import project.*
import project.Simulant
import project.config.*

import scala.swing.*
import scala.swing.event.ButtonClicked
import scala.collection.parallel.immutable.ParSeq
import scala.concurrent.*
import java.awt.{Color, Graphics2D, RenderingHints}
import java.awt.event.{ActionEvent, ActionListener}
import java.awt.geom.Ellipse2D


object SimGUI extends SimpleSwingApplication {

  /** for GUI */
  private val areaWidth = readMapSize._1
  private val areaHeight = readMapSize._2
  private val settingsWidth = readMapSize._1/2
  private val settingsHeight = readMapSize._2

  /** for the FPS counter */
  private var frameCount = 0
  private var lastUpdate = System.currentTimeMillis()
  private var frameRate = 0

  /** Stuff for the sim itself */
  var leader: Simulant = Simulant(true)
  var followers: ParSeq[Simulant] = ParSeq.fill(readFollowerNum)(Simulant(false))
  private var simSpeed: Int = 1

  /** main frame is the main window */
  def top: Frame = new MainFrame:
    title = "Follow the leader sim"

    /** Area panel is the visual part of the simulation. It draws the simulants to a map with Graphics2D and even counts the frames for debugging and benchmarking purposes */
    val areaPanel = new BoxPanel(Orientation.Vertical){
      preferredSize = new Dimension(areaWidth, areaHeight)
      override def paintComponent(pic: Graphics2D) =
        frameCount += 1
        pic.setColor(new Color(40, 40, 60))
        pic.fillRect(0, 0, areaWidth, areaHeight)
        pic.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON)
        pic.setColor(Color.RED)
        followers.foreach(i => pic.fill(Ellipse2D.Double(i.readPos._1, i.readPos._2, 10, 10)))
        pic.setColor(Color.BLUE)
        pic.fill(Ellipse2D.Double(leader.readPos._1, leader.readPos._2, 10, 10))
        val now = System.currentTimeMillis()
        val elapsed = now - lastUpdate
        if (elapsed > 1000)
          frameRate = (frameCount * 1000 / elapsed).toInt
          frameCount = 0
          lastUpdate = now
        pic.setColor(Color.WHITE)
        pic.drawString(s"Frame rate: $frameRate", 10, 20)
    }

    /** Settings panel is the other half of the GUI, this panel hosts most of the interactive bits for the user to change the simulation. As of 30.3, most of the things here are place holders */
    val settingsPanel = new BoxPanel(Orientation.Vertical):
      preferredSize = new Dimension(settingsWidth, settingsHeight)
      for i <- 1 to 8 do
        contents += new BoxPanel(Orientation.Horizontal):
          contents += new Label(s"Setting $i: ")
          contents += new TextField(10)
        contents += Swing.VStrut(5)
      val step =  new Button("One step")
      val reboot = new Button("restart")
      val pauseb = new Button("pause/play")
      var pauseIcon = new Label("paused")
      //contents += pauseIcon
      contents += pauseb
      contents += reboot
      contents += step

      /** reactions to the buttons */
      step.reactions += {
        case ButtonClicked(_) =>
          update()
          areaPanel.repaint()}

      reboot.reactions += {
        case ButtonClicked(_) =>
          restart()
          areaPanel.repaint()}
      pauseb.reactions += {
        case ButtonClicked(_) =>
          pause()
          if isPaused then pauseIcon.text = "paused" else pauseIcon.text = "playing"}

    /** splitPanel is simply used to orient the two previous panels properly.  */
    val splitPane = new SplitPane(Orientation.Vertical, settingsPanel, areaPanel):
      dividerLocation = settingsWidth

    contents = splitPane

  /** timing logic for the animation is here, timer ticks every 5 ms and it allows a smooth and pleasant movement for the areaPanel */
    val listener = new ActionListener() {
      def actionPerformed(e: java.awt.event.ActionEvent) =
        update()
        areaPanel.repaint()
    }

    val timer = new javax.swing.Timer(5, listener)
    timer.start()



  /** Changes the sim speed. Sim speed can be a integer from 1 to 10 */
  def changeSimSpeed(x: Int) =
    simSpeed = if x < 1 then 1 else if 10 < x then 10 else x

  def sysClock(): Unit =
    wait(100)

  def restart() =
    followers.foreach(_.restart())
    leader.restart()

  def pause() =
    wait(10)
    isPaused = !isPaused

  def update() =
  // if isPaused then wait(10) else
     followers.foreach(_.update())
     leader.update()


}

