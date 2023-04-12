package project

import project.*
import play.api.libs.json
import project.configGeneric
import project.configS

import scala.swing.*
import scala.swing.event.{ButtonClicked, ValueChanged}
import java.io.{File, PrintWriter}
import play.api.libs.json.*

import javax.swing.WindowConstants

/** This is the object to read and write files. It also provides a GUI window for easier usage */

object FileReader:

  var filePath: String = "fgsfds.jpg"
  val chooser = new FileChooser()

  val fileChooserWindow = new MainFrame:
      title = "File Chooser"
      contents = new BoxPanel(Orientation.Vertical):
        contents += chooser
        border = Swing.EmptyBorder(10)

  fileChooserWindow.pack()

  def checkPath = ???

  def loadSettings() =
    configS.fromSpecific(configGeneric(1,2,3,4,100,(800,800),5))
    SimGUI.restart()

  def read(filePath: String): JsValue = 
    val jsonString = scala.io.Source.fromFile(filePath).mkString
    Json.parse(jsonString)

  def write(filePath: String, data: JsValue): Unit = 
    val file = new File(filePath)
    val writer = new PrintWriter(file)
    try 
      writer.write(Json.prettyPrint(data))
    finally 
      writer.close()


