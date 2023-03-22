//this class has been abandoned following problems updating the picture when calling method draw()

/** package project

import project.config.*
import project.Simulant

import java.awt.geom.Ellipse2D
import java.awt.image.*
import java.awt.{BasicStroke, Color, Font, Graphics2D, RenderingHints}
import java.io.File
import javax.imageio.ImageIO
import scala.collection.parallel.immutable.ParSeq

class simulationDraw(leader: Simulant, followers: ParSeq[Simulant]):

  val test = ImageIO.read(new File("fgsfds.jpg"))

  def draw(): BufferedImage =
    var background = new BufferedImage(readMapSize._1,readMapSize._2, BufferedImage.TYPE_INT_RGB)
    val pic = background.createGraphics()
    pic.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON)
    pic.setColor(Color.RED)
    followers.foreach(i => pic.fill(Ellipse2D.Double(i.readPos._1,i.readPos._2,10,10)))
    pic.setColor(Color.BLUE)
    pic.fill(Ellipse2D.Double(leader.readPos._1,leader.readPos._2,10,10))
    pic.dispose()
    background



  def testImage = test


*/