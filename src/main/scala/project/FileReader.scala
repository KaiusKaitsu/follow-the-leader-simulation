package project

import project.*
import play.api.libs.json

import java.io.{File, PrintWriter}
import play.api.libs.json._

object FileReader:

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


