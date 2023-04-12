package project

import project.*
import project.Simulant
import project.configS

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
  private val areaWidth = configS.readMapSize._1
  private val areaHeight = configS.readMapSize._2
  private val settingsWidth = configS.readMapSize._1/2
  private val settingsHeight = configS.readMapSize._2

  /** for the FPS counter */
  private var frameCount = 0
  private var lastUpdate = System.currentTimeMillis()
  private var frameRate = 0

  /** Stuff for the sim itself */
  var leader: Simulant = Simulant(true)
  var followers: ParSeq[Simulant] = ParSeq.fill(configS.readFollowerNum)(Simulant(false))

  /** for choosing files */
  val chooser = FileReader.fileChooserWindow

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

    /** Settings panel is the other half of the GUI, this panel hosts most of the interactive bits for the user to change the simulation. */
    val settingsPanel = new BoxPanel(Orientation.Vertical):
        preferredSize = new Dimension(settingsWidth, settingsHeight)

      /** Here are the controlls for the usage of the FileReader */
        val boxTxt = "Please write here"
        val fileReadInput = new Label("Path to .json config file")
        val fileReadTxt = new TextField(boxTxt) //write the path of the wanted configS file here
        val loadButton = new Button("Load file") //actually loads the file specified in the txt field
        val findButton = new Button("File explorer") // allows user to browse with file explorer to find the wanted configS file. Doesnt actually load the file only writes it in the txt field
        val feedback = new Label("No file loaded") // gives feedback when user uses the loadButton. Gives hints like "file not found" or "file loaded succesfully" etc
        feedback.foreground = Color.RED
        
        val step = new Button("One step")
        val reboot = new Button("restart")
        val pauseb = new Button("paused")
        
        val a = createSlider("Leader mass", configS.readLeaderMass, configS.changeLeaderMass, 1, 100, false)
        val b = createSlider("Follower mass", configS.readFollowerMass, configS.changeFollowerMass, 1, 100, false)
        val c = createSlider("Leader speed", configS.readLeaderSpeed, configS.changeLeaderSpeed, 1, 100, false)
        val d = createSlider("Follower speed", configS.readFollowerSpeed, configS.changeFollowerSpeed, 1, 100, false)
        val e = createSlider("Follower number", configS.readFollowerNum, configS.changeFollowerNum, 1, 100, true)
        val f = createSlider("Simulation speed", configS.readSimSpeed, timer.setDelay, 1, 100, false)
        val sliders = Seq(c,d,a,b,e,f)

        contents ++= sliders
        contents ++= Seq(pauseb, reboot, step)
        contents ++= Seq(fileReadInput, fileReadTxt, loadButton, findButton, feedback)

        /** reactions to the buttons */
        step.reactions += {
          case ButtonClicked(_) =>
            project.SimGUI.update()
            areaPanel.repaint()}

        reboot.reactions += {
          case ButtonClicked(_) =>
            restart()
            areaPanel.repaint()}

        pauseb.reactions += {
          case ButtonClicked(_) =>
            pause()
            if configS.isPaused then pauseb.text = "paused" else pauseb.text = "playing"}

        loadButton.reactions += {
         case ButtonClicked(_) =>
           FileReader.loadSettings()
           a.slider.value = configS.readLeaderMass
           b.slider.value = configS.readFollowerMass
           c.slider.value = configS.readLeaderSpeed
           d.slider.value = configS.readFollowerSpeed
           e.slider.value = configS.readFollowerNum
           f.slider.value = configS.readSimSpeed
          }

        findButton.reactions += {
          case ButtonClicked(_) =>
            chooser.visible = true }



    /** splitPanel is simply used to orient the two previous panels properly.  */
    val splitPane = new SplitPane(Orientation.Vertical, settingsPanel, areaPanel):
      dividerLocation = settingsWidth

    contents = splitPane

  /** timing logic for the animation is here, timer ticks every 5 ms and it allows a smooth and pleasant movement for the areaPanel */
    val listener = new ActionListener() {
      def actionPerformed(e: java.awt.event.ActionEvent) =
        if configS.isPaused then Thread.sleep(10) else {
        update()
        areaPanel.repaint()}}

    val timer = new javax.swing.Timer(configS.readSimSpeed, listener)
    timer.start()

  def restart() =
    followers = ParSeq.fill(configS.readFollowerNum)(Simulant(false))
    leader.restart()

  def pause() =
    configS.isPaused = !configS.isPaused

  def update() =
     followers.foreach(_.update())
     leader.update()


/** This is a class to easily create many sliders for the GUI without repetition. Constructor "write" takes a method from userInput to change the values for the simulation. */
private class createSlider(name1: String, var read: Int, var write: Int => Unit, minn: Int, maxx: Int, reqRestart: Boolean) extends BoxPanel(Orientation.Vertical){

  val slider = new Slider {
    orientation = Orientation.Horizontal
    min = minn
    max = maxx
    value = read
    paintTicks = true
    minimumSize = new Dimension(100, 200)
  }

  val label = new Label {
    text = s"$name1  =  ${slider.value.toString} "}

  listenTo(slider)

  reactions += {
    case a: ValueChanged =>
     write(slider.value)
     label.text = s"$name1  =  ${slider.value.toString}"
     if reqRestart then restart()}
  contents += label
  contents += slider

  def reload() =
    slider.value = read
    label.text = s"$name1  =  ${read.toString}"
}}