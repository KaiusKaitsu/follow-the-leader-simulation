package project

import project.config.*
import project.Simulant
import java.awt.geom.Ellipse2D
import java.awt.image.*
import java.awt.{BasicStroke, Color, Font, Graphics2D}
import java.io.File
import javax.imageio.ImageIO
import scala.collection.parallel.immutable.ParSeq

class simulationDraw(leader: Simulant, followers: ParSeq[Simulant]):

  var background = new BufferedImage(readMapSize._1,readMapSize._2, BufferedImage.TYPE_INT_RGB)
  var test = ImageIO.read(new File("fgsfds.jpg"))


  def draw() =
    val pic = background.createGraphics()
    pic.setColor(Color.RED)
    followers.foreach(i => pic.draw(Ellipse2D.Double(i.readPos._1,i.readPos._2,10,10)))
    pic.setColor(Color.BLUE)
    pic.draw(Ellipse2D.Double(leader.readPos._1,leader.readPos._2,10,10))
    pic.dispose()
    pic

  def testImage = test


