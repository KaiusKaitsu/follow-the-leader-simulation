package project

import project.*
import project.Simulant
import project.config.*

import scala.swing.*
import scala.swing.event.{ButtonClicked, ValueChanged}
import scala.collection.parallel.immutable.ParSeq
import scala.concurrent.*
import java.awt.{Color, Graphics2D, RenderingHints}
import java.awt.event.{ActionEvent, ActionListener}
import java.awt.geom.Ellipse2D
import javax.swing.plaf.SliderUI
import scala.collection.mutable.HashMap

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
    resizable = false

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
        /** fps counter */
        val now = System.currentTimeMillis()
        val elapsed = now - lastUpdate
        if (elapsed > 1000)
          frameRate = (frameCount * 1000 / elapsed).toInt
          frameCount = 0
          lastUpdate = now
        pic.setColor(Color.WHITE)
        pic.drawString(s"Frame rate: $frameRate", 10, 20)}

    /** Settings panel is the other half of the GUI, this panel hosts most of the interactive bits for the user to change the simulation. As of 30.3, most of the things here are place holders */
    val settingsPanel = new BoxPanel(Orientation.Vertical):
        preferredSize = new Dimension(settingsWidth, settingsHeight)

        val boxTxt = "Please write here"
        val followerNum = new Label("Number of followers")
        val followerNumTxt = new TextField(boxTxt, 1)
        
        val step = new Button("One step")
        val reboot = new Button("restart")
        val pauseb = new Button("paused")
        
        contents += createSlider("Leader mass", readLeaderMass, changeLeaderMass, 1, 100)
        contents += createSlider("Follower mass", readFollowerMass, changeFollowerMass, 1, 100)
        contents += createSlider("Leader speed", readLeaderSpeed, changeLeaderSpeed, 1, 100)
        contents += createSlider("Follower speed", readFollowerSpeed, changeFollowerSpeed, 1, 100)
        contents ++= Seq(followerNum, followerNumTxt)
        contents += createSlider("Simulation speed", readSimSpeed, timer.setDelay, 1, 100)
        contents ++= Seq(pauseb, reboot, step)



        /** reactions to the buttons */
        step.reactions += {
          case ButtonClicked(_) =>
            project.SimGUI.update()
            areaPanel.repaint()
        }

        reboot.reactions += {
          case ButtonClicked(_) =>
            restart()
            areaPanel.repaint()}

        pauseb.reactions += {
          case ButtonClicked(_) =>
            pause()
            if isPaused then pauseb.text = "paused" else pauseb.text = "playing"}

    /** splitPanel is simply used to orient the two previous panels properly.  */
    val splitPane = new SplitPane(Orientation.Vertical, settingsPanel, areaPanel):
      dividerLocation = settingsWidth

    contents = splitPane

  /** timing logic for the animation is here, timer ticks every 5 ms and it allows a smooth and pleasant movement for the areaPanel */
    val listener = new ActionListener() {
      def actionPerformed(e: java.awt.event.ActionEvent) =
        if isPaused then Thread.sleep(10) else {
        update()
        areaPanel.repaint()}}
    val timer = new javax.swing.Timer(readSimSpeed, listener)
    timer.start()

  def restart() =
    followers.foreach(_.restart())
    leader.restart()

  def pause() =
    isPaused = !isPaused

  def update() =
     followers.foreach(_.update())
     leader.update()


class createSlider(name1: String, read: Int, write: Int => Unit, minn: Int, maxx: Int) extends BoxPanel(Orientation.Vertical){

  val slider = new Slider {
    orientation = Orientation.Horizontal
    min = minn
    max = maxx
    value = read
    paintTicks = true
    minimumSize = new Dimension(100, 200)
  }

  val label = new Label {
    text = s"$name1  =  ${slider.value.toString} "
  }
  // listen for changes to the slider's value
  listenTo(slider)

  // update the label and configVar when the slider's value changes
  reactions += {
    case a: ValueChanged =>
      write(slider.value)
      label.text = s"$name1  =  ${slider.value.toString}"
  }
  contents += label
  contents += slider
  border = Swing.EmptyBorder(10, 10, 10, 10)
}
}