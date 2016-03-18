import java.awt.{Font, Dimension}
import scala.concurrent.Future
import scala.swing.event.{ValueChanged, ButtonClicked}
import scala.swing._
import scala.util.{Failure, Success}
import scala.concurrent.ExecutionContext.Implicits.global
import StringUtils._

class SpitterWindow(editorPane: EditorPane, globalVars: GlobalVars) {

  // Start-Pause-Continue button
  val startButton = new Button {
    text = "Start Spitting"
    requestFocus()
    peer.setEnabled(true)
  }

  val stopButton = new Button {
    text = "Stop Spitting"
    peer.setEnabled(false)
  }

  // Text editor area properties
  val outputArea = new EditorPane() {
    font = new swing.Font("Monospace", java.awt.Font.PLAIN, 100)
    preferredSize = new swing.Dimension(800, peer.getFontMetrics(peer.getFont).getHeight)
    focusable = false
    editable = false
  }

  val speedArea = new TextField {
    font = new Font("Monospace", java.awt.Font.PLAIN, 12)
    preferredSize = new Dimension(40, 20)
    focusable = false
    editable = false
    columns = 3
  }

  val speedSlider = new Slider {
    min = 60
    max = 1200
    value = 350
  }

  val speedLabel1 = new Label() {
    font = new Font("Monospace", java.awt.Font.PLAIN, 12)
    text = "  Change the speed of the text:"
    focusable = false
  }
  val speedLabel2 = new Label() {
    font = new Font("Monospace", java.awt.Font.PLAIN, 12)
    text = " words per second  "
    focusable = false
  }



  val frame = new Frame {

    title = "Spitter"
    visible = true
    resizable = false

    peer.setAlwaysOnTop(true)
    // Place it in the centre
    peer.setLocationRelativeTo(null)
    contents = new BorderPanel {

      add(outputArea, BorderPanel.Position.Center)

      val buttonP = new BoxPanel(Orientation.Vertical){
        contents += new BorderPanel
        contents += startButton
        contents += stopButton
        contents += new BorderPanel
      }

      val speedP = new BorderPanel{
        layout += speedLabel1 -> BorderPanel.Position.West
        layout += speedSlider -> BorderPanel.Position.Center
        layout += new BorderPanel {
          layout += speedArea -> BorderPanel.Position.Center
          layout += speedLabel2 -> BorderPanel.Position.East
        } -> BorderPanel.Position.East
      }
      add(speedP, BorderPanel.Position.South)
      add(buttonP, BorderPanel.Position.East)
    }

    def loadSpitter() = {
      val spitter = new SpitterEngine(editorPane, outputArea, globalVars, speedSlider)
      val (startIterator, usableIterator) = spitter.getTextFromEditor.toIterator.duplicate
      (spitter, startIterator, usableIterator)
    }

    def showOneDecimal(str: String): String = {
      val splitIt = str.split('.')
      splitIt(0)+"."+splitIt(1)(0)
    }

    def changeTextToSpeedSliderValue() = {
      val wps = (1.0*speedSlider.value/60).toString
      speedArea.text = showOneDecimal(wps)
    }

    def clickOn_startButton(): Unit ={
      if(startButton.text == "Start Spitting") {
        load = loadSpitter()
        spitter = load._1
        startIterator = load._2
        usableIterator = load._3
        spitter.stopIt = false
        startButton.text = "     Pause     "
        stopButton.peer.setEnabled(true)
        spitter.spit(usableIterator)
      }
      else if(startButton.text == "     Pause     ") {
        spitter.stopIt = true
        startButton.text = "   Continue   "
      }
      else if(startButton.text == "   Continue   ") {
        spitter.stopIt = false
        spitter.spit(usableIterator)
        startButton.text = "     Pause     "
      }
    }

    def clickOn_stopButton(): Unit ={
      spitter.stopIt = true
      val newIterator = startIterator.duplicate
      startIterator = newIterator._1
      usableIterator = newIterator._2
      startButton.peer.setEnabled(true)
      startButton.text = "Start Spitting"
      stopButton.peer.setEnabled(false)
    }

    var load = loadSpitter()
    var spitter = load._1
    var startIterator = load._2
    var usableIterator = load._3


    changeTextToSpeedSliderValue()
    val colorMaker = new GeneralColoring(outputArea)
    colorMaker.theme = globalVars.theme

    listenTo(startButton, stopButton, speedSlider)

    reactions += {
      case ButtonClicked(`startButton`) =>
        clickOn_startButton()
      case ButtonClicked(`stopButton`) =>
        clickOn_stopButton()
      case ValueChanged(`speedSlider`) =>
        changeTextToSpeedSliderValue()
    }
  }
}


class SpitterEngine(editorPane: EditorPane,
                    outputArea: EditorPane,
                    globalVars: GlobalVars,
                    slider: Slider) {

  var stopIt = false

  val areaWidth = outputArea.peer.getWidth

  val averageWordLength = {
    val lst = getTextFromEditor
    val len = lst.length
    if ( len > 0 ) {
      lst.map(_.length).sum/len
    } else 5 // could be anything other than zero
  }

  def getTextFromEditor = clearFromSpecialChars(editorPane.text)
    .split(" ")
    .map(_.trim)
    .filter(! _.isEmpty)

  def spit(textIterator: Iterator[String]): Unit = {
    if (textIterator.hasNext && !stopIt ) {
      val paste = Future {
        val word = textIterator.next
        centreMessage(word)
        Thread.sleep(correctDuration(word).getOrElse(500))

      }
      paste.onComplete {
        case Success(s) => spit(textIterator)
        case Failure(f) => outputArea.text = ""
      }
    } else {
      outputArea.text = ""
    }
  }

  def centreMessage(message: String) = {
    val asc = outputArea.peer.getFontMetrics(outputArea.peer.getFont).getAscent
    val des = outputArea.peer.getFontMetrics(outputArea.peer.getFont).getDescent
    val letter_length = asc - des
    val messageLength = message.length * letter_length
    val spaces = " "*((areaWidth + 200 - messageLength)/letter_length)
    outputArea.text = spaces+message
  }

  // Given 'millis' it returns 'wpm' and given
  // 'wpm' it returns 'millis'
  def millis_wpm(x: Long): Option[Long] = {
    if (x == 0) None else Some(60000/x)
  }

  def correctDuration(word: String): Option[Long] = {
    val word_len = word.length
    val t = slider.value*averageWordLength/word_len
    val ans = if (t > 500) 500 else if (t < 150) 150 else t
    millis_wpm(ans)
  }
}


