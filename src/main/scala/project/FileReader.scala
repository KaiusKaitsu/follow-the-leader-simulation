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

/** This is the object to read and write files. It also provides a GUI window for easier usage. The save location is a folder called "Saved config files" in the project root
 * This object uses playframeworks json tools for scala to be help parsing and writing json. */

object FileReader:

  /** getConfig gives an optioned configGeneric from a filename if it exists in the folder else None. Reading files works with good ol' java */
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

  /** reads a given file with getConfig to actually apply the settings to the simulation (if the read was succesfull). returns if the read was succesfull or not */
  def read(filename: String): Boolean =

    val config = getConfig(filename)
    if config.isEmpty then
      false
    else
      configS.fromSpecific(config.get)
      SimGUI.restart()
      true

  /** writes a new file with a name to "Saved config file" folder and returns true. If there exists a file with a certain name then it returns false and no writing has taken place. */
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

end FileReader




