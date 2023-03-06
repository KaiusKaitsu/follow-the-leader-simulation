package project
import scala.swing.*
import project.simulationDraw.*

object SimGUI extends SimpleSwingApplication {
  private val areaWidth = 500
  private val areaHeight = 500

  private val settingsWidth = 250
  private val settingsHeight = 500

  def top: Frame = new MainFrame:
    title = "Follow the leader sim"

    val areaPanel = new Panel:
      preferredSize = new Dimension(areaWidth, areaHeight)
      background = java.awt.Color.WHITE

    val settingsPanel = new BoxPanel(Orientation.Vertical):
      preferredSize = new Dimension(settingsWidth, settingsHeight)

      for i <- 1 to 10 do
        contents += new BoxPanel(Orientation.Horizontal):
          contents += new Label(s"Setting $i: ")
          contents += new TextField(10)
        contents += Swing.VStrut(5)

    val splitPane = new SplitPane(Orientation.Vertical, settingsPanel, areaPanel):
      dividerLocation = settingsWidth

    contents = splitPane
}

