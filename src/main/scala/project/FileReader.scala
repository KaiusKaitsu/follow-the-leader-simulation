package project

import project.*
import project.configGeneric
import project.configS.format
import project.configS

import scala.swing.*
import scala.swing.event.{ButtonClicked, ValueChanged}
import java.io.{BufferedWriter, File, FileInputStream, PrintWriter}
import play.api.libs.json.*
import play.api.libs.json.Format.GenericFormat
import play.api.libs.json.OFormat.oFormatFromReadsAndOWrites

import javax.swing.WindowConstants

/** This is the object to read and write files. It also provides a GUI window for easier usage */

object FileReader:

  var filePath: String = "Saved config files/XD"
  val chooser = new FileChooser()

  val fileChooserWindow = new MainFrame:
      title = "File explorer"
      contents = chooser
  fileChooserWindow.pack()

  def getConfig(filename: String): Option[configGeneric] =
    val file = new File(s"Saved config files/$filename")
    if file.exists() && file.isFile then
      val inputStream = new FileInputStream(file)
      try
        val json = Json.parse(inputStream)
        Json.fromJson[configGeneric](json).asOpt
      catch
        case e: Exception => None
      finally
        inputStream.close()
    else
      None

  def read(filename: String): Boolean =

    val config = getConfig(filename)
    if config.isEmpty then
      false
    else
      configS.fromSpecific(config.get)
      SimGUI.restart()
      true


  def write(filename: String): Boolean =
    val file = new File(s"Saved config files/$filename")
    if !file.exists() then
      val json = Json.toJson(configS)
      val writer = new PrintWriter(file)
      try
        writer.write(Json.prettyPrint(json))
      catch
        case e: Exception => false
      finally
        writer.close()
      true
    else
      false



