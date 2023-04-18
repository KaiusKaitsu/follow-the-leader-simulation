package project

import project.*
import project.Simulant
import project.configS

import scala.swing.*
import scala.swing.event.{ButtonClicked, MouseClicked, MouseMoved, ValueChanged}

import scala.collection.parallel.immutable.ParSeq
import scala.collection.immutable.Seq
import scala.concurrent.*
import scala.collection.mutable.HashMap

import java.awt.{Color, Graphics2D, RenderingHints}
import java.awt.event.{ActionEvent, ActionListener}
import java.awt.geom.Ellipse2D
import javax.swing.plaf.SliderUI
import java.awt.Font



/** this is the heart of the program and also where it is started. This rather big object creates the graphical user interface, runs the program clock and gives orders to the simulants.
 * Although it would be nice to chop it to smaller pieces it in reality isn't really possible since most if not all the things here are depended of each other and although big,
 * this object certainly isnt massive. */

object SimGUI extends SimpleSwingApplication:

  /** values for GUI */
  private val areaWidth = configS.readMapSize._1
  private val areaHeight = configS.readMapSize._2
  private val settingsWidth = configS.readMapSize._1/2
  private val settingsHeight = configS.readMapSize._2
  private val globalfont = new Font("SansSerif", Font.BOLD, 16)

  /** for the FPS counter */
  private var frameCount = 0
  private var lastUpdate = System.currentTimeMillis()
  private var frameRate = 0

  /** Stuff for the sim itself */
  var leader: Simulant = Simulant(true)
  var followers: ParSeq[Simulant] = ParSeq.fill(configS.readFollowerNum)(Simulant(false))
  private var trackingToggle: Boolean = false

  /** main frame is the main window */
  def top: Frame = new MainFrame:

    title = "Follow the leader simulation"
    resizable = false

    /** Area panel is the visual part of the simulation. It draws the simulants to a map with Graphics2D and even counts the frames for debugging and benchmarking purposes
     * also area panel allows mouse targeting when the cursor is on it. The targeting is toggled with clicking on the areaPanel with mouse */
    val areaPanel = new BoxPanel(Orientation.Vertical):
      maximumSize = new Dimension(areaWidth, areaHeight)
      minimumSize = new Dimension(areaWidth, areaHeight)
      preferredSize = new Dimension(areaWidth, areaHeight)

      /** this is what feeds the position to leaders target if trackingToggle is enabled */
      listenTo(mouse.moves, mouse.clicks)
      reactions += {
        case e: MouseClicked => trackingToggle = !trackingToggle
        case e: MouseMoved => if trackingToggle then leader.mouseTarget(e.point.x, e.point.y)
      }
      override def paintComponent(pic: Graphics2D) =
        frameCount += 1
        pic.setColor(new Color(40, 40, 60))
        pic.fillRect(0, 0, areaWidth, areaHeight)
        pic.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON)
        pic.setColor(Color.RED)
        followers.foreach(i => pic.fill(Ellipse2D.Double(i.readPos._1, i.readPos._2, 10, 10)))
        pic.setColor(Color.BLUE)
        pic.fill(Ellipse2D.Double(leader.readPos._1, leader.readPos._2, 10, 10))

        /** fps counter logic is here */
        val now = System.currentTimeMillis()
        val elapsed = now - lastUpdate
        if (elapsed > 1000)
          frameRate = (frameCount * 1000 / elapsed).toInt
          frameCount = 0
          lastUpdate = now
        pic.setColor(Color.WHITE)
        pic.drawString(s"Frame rate: $frameRate", 10, 20)
        if trackingToggle then pic.drawString("Mouse tracking on",10,40) else pic.drawString("Mouse tracking off",10,40)

    end areaPanel



    /** Settings panel is the other half of the GUI, this panel hosts most of the interactive bits for the user to change the simulation.
     * For the sliders createSlider class is used. This panel is quite volatile to changing the simulations map size which is one reason this setting isnt given to end user */
    val settingsPanel = new BoxPanel(Orientation.Vertical):

        preferredSize = new Dimension(settingsWidth, settingsHeight)
        maximumSize = new Dimension(settingsWidth, settingsHeight)
        minimumSize = new Dimension(settingsWidth, settingsHeight)

      /** Here are the controlls for the usage of the FileReader */
        val boxTxt = "Please write here"
        val fileReadInput = new Label("Load config file with name:")
        val fileReadTxt = new TextField(boxTxt) //write the path of the wanted configS file here
        val loadButton = new Button("Load") //actually loads the file specified in the txt field
        val feedback = new Label("No file loaded") // gives feedback when user uses the loadButton. Gives hints like "file not found" or "file loaded succesfully" etc
        val saveCurrent = new Label("Save curret config with name")
        val save = new Button("Save")
        val saveName = new TextField(boxTxt)
        val saveFeedback = new Label("Not saved")
        feedback.foreground = Color.GRAY
        saveFeedback.foreground = Color.GRAY

      /** and the container for the file controlls */
        val container = new GridPanel(9,1):
          val apu = Seq(fileReadInput, fileReadTxt, loadButton, feedback, saveCurrent, saveName, save, saveFeedback)
          apu.foreach(_.font=globalfont)
          contents ++= apu

       /** These are the controlls for the playing of the simulation like rebooting, pausing and moving one step */
        val step = new Button("One step")
        val reboot = new Button("restart")
        val pauseb = new Button("paused")

      /** the container for the sim controlls */
        val container2 = new GridPanel(3,1):
          val apu = Seq(step, reboot, pauseb)
          apu.foreach(_.font=globalfont)
          contents ++= apu

       /** helper to change the simulation clock with the create slider class */
        def timerHelper(x: Int): Unit =
          timer.setDelay(x)
          configS.changeSimSpeed(x)

        /** here all the sliders area created */
        val a = createSlider("Leader mass", configS.readLeaderMass, configS.changeLeaderMass, 1, 100, false)
        val b = createSlider("Follower mass", configS.readFollowerMass, configS.changeFollowerMass, 1, 100, false)
        val c = createSlider("Leader speed", configS.readLeaderSpeed, configS.changeLeaderSpeed, 1, 100, false)
        val d = createSlider("Follower speed", configS.readFollowerSpeed, configS.changeFollowerSpeed, 1, 100, false)
        val e = createSlider("Follower number", configS.readFollowerNum, configS.changeFollowerNum, 1, 100, true)
        val f = createSlider("Simulation speed", configS.readSimSpeed, timerHelper, 1, 100, false)
        val sliders = ParSeq(c,d,a,b,e,f)

      /** Adding all the components to the settingsPanel */
        contents ++= sliders
        contents += container2
        contents += container

      /** reactions to the various buttons */

        /** take one step in the simulation */
        step.reactions += {
          case ButtonClicked(_) =>
            project.SimGUI.update()
            areaPanel.repaint()}

        /** restart the simulation */
        reboot.reactions += {
          case ButtonClicked(_) =>
            restart()
            areaPanel.repaint()}

        /** pause the simulation */
        pauseb.reactions += {
          case ButtonClicked(_) =>
            pause()
            if configS.isPaused then pauseb.text = "paused" else pauseb.text = "playing"}

        /** load a config file with name from fileReadTxt with FileReader */
        loadButton.reactions += {
         case ButtonClicked(_) =>
           if FileReader.read(fileReadTxt.text) then
             a.slider.value = configS.readLeaderMass
             b.slider.value = configS.readFollowerMass
             c.slider.value = configS.readLeaderSpeed
             d.slider.value = configS.readFollowerSpeed
             e.slider.value = configS.readFollowerNum
             f.slider.value = configS.readSimSpeed
             feedback.text = s"File '${fileReadTxt.text}' succesfully loaded"
             feedback.foreground = Color(0,179,6)
             else
               feedback.text = "File not found"
               feedback.foreground = Color.RED}

        /** Save the current settings to a json file with name saveName to project root folder "Saved config files" */
        save.reactions += {
          case ButtonClicked(_) =>
            if FileReader.write(saveName.text) then
              saveFeedback.text = "File writen succesfully"
              saveFeedback.foreground = Color(0,179,6)
            else
              saveFeedback.text = "File with this name already exists"
              saveFeedback.foreground = Color.RED
          }

    end settingsPanel

    /** splitPanel is simply used to orient the two previous panels properly side by side */
    val splitPane = new SplitPane(Orientation.Vertical, settingsPanel, areaPanel):
      dividerLocation = settingsWidth

    /** this adds all the components to the GUI mainframe */
    contents = splitPane

  /** timing logic for the animation is here, timer ticks every 5 ms and it allows a smooth and pleasant movement for the areaPanel */
    val listener = new ActionListener() {
      def actionPerformed(e: java.awt.event.ActionEvent) =
        if configS.isPaused then areaPanel.repaint() else {
        update()
        areaPanel.repaint()
        }}

    val timer = new javax.swing.Timer(configS.readSimSpeed, listener)
    timer.start()


  /** this method restarts all the simulants */
  def restart() =
    followers = ParSeq.fill(configS.readFollowerNum)(Simulant(false))
    leader.restart()

  /** pauses the simulation */
  def pause() =
    configS.isPaused = !configS.isPaused

  /** updates the simulation by calling the update() method on all the simulants */
  def update() =
     followers.foreach(_.update())
     leader.update()


  /** This is a class to easily create many sliders for the GUI without repetition. Constructor "write" takes a method of userInput to change the values for the simulation.
   * returns a ready to use boxPanel for the GUI*/
  private class createSlider(name1: String, var read: Int, var write: Int => Unit, minn: Int, maxx: Int, reqRestart: Boolean) extends BoxPanel(Orientation.Vertical):

    /** create the actual slider with the given values */
    val slider = new Slider {
      orientation = Orientation.Horizontal
      min = minn
      max = maxx
      value = read
      paintTicks = true
      minimumSize = new Dimension(100, 200)
    }

    /** label the slider */
    val label = new Label {
      text = s"$name1  =  ${slider.value.toString} "
      font = globalfont
    }

    /** And add functionality to the slider! */
    listenTo(slider)

    reactions += {
      case a: ValueChanged =>
        write(slider.value)
        label.text = s"$name1  =  ${slider.value.toString}"
        if reqRestart then restart()
    }
    contents += label
    contents += slider

    /** this is used when a new config file is used so that the sliders correspond to the new settings */
    def reload() =
      slider.value = read
      label.text = s"$name1  =  ${read.toString}"
  end createSlider

end SimGUI

