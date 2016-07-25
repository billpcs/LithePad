package gui

import java.awt.{Dimension, Font}

import colors.GeneralColoring
import textengine.{Cluster, StringUtils}
import StringUtils._

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.swing._
import scala.swing.event.{ButtonClicked, ValueChanged}
import scala.util.{Failure, Success}

class SpitterWindow(cluster: Cluster) {

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
  val outputArea = new TextArea() {
    font = new Font("Monospace", java.awt.Font.PLAIN, 100)
    preferredSize =
      new Dimension(800, peer.getFontMetrics(peer.getFont).getHeight)
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

}

class SpitterEngine(cluster: Cluster, outputArea: TextArea, slider: Slider) {

  var stopIt = false

  val areaWidth = outputArea.peer.getWidth

  val averageWordLength = {
    val lst = getTextFromEditor
    val len = lst.length
    if (len > 0) {
      lst.map(_.length).sum / len
    } else 5 // could be anything other than zero
  }

  def getTextFromEditor =
    clearFromSpecialChars(cluster.editor.text)
      .split(" ")
      .map(_.trim)
      .filter(!_.isEmpty)

  def spit(textIterator: Iterator[String]): Unit = {
    if (textIterator.hasNext && !stopIt) {
      val paste = Future {
        val word = textIterator.next
        centerMessage(word)
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

  def centerMessage(message: String) = {
    val asc = outputArea.peer.getFontMetrics(outputArea.peer.getFont).getAscent
    val des =
      outputArea.peer.getFontMetrics(outputArea.peer.getFont).getDescent
    val letter_length = asc - des
    val messageLength = message.length * letter_length
    val spaces = " " * ((areaWidth + 200 - messageLength) / letter_length)
    outputArea.text = spaces + message
  }

  // Given 'millis' it returns 'wpm' and given
  // 'wpm' it returns 'millis'
  def millis_wpm(x: Long): Option[Long] = {
    if (x == 0) None else Some(60000 / x)
  }

  def correctDuration(word: String): Option[Long] = {
    val word_len = word.length
    val t = slider.value * averageWordLength / word_len
    val ans = if (t > 500) 500 else if (t < 150) 150 else t
    millis_wpm(ans)
  }
}

object SpitterWindow {

  def spitterWindow(cluster: Cluster) = {

    val win = new SpitterWindow(cluster)

    new Frame {
      title = "Spitter"
      visible = true
      resizable = false
      peer.setAlwaysOnTop(true)
      // Place it in the centre
      peer.setLocationRelativeTo(null)
      contents = new BorderPanel {
        add(win.outputArea, BorderPanel.Position.Center)
        val buttonP = new BoxPanel(Orientation.Vertical) {
          contents += new BorderPanel
          contents += win.startButton
          contents += win.stopButton
          contents += new BorderPanel
        }
        val speedP = new BorderPanel {
          layout += win.speedLabel1 -> BorderPanel.Position.West
          layout += win.speedSlider -> BorderPanel.Position.Center
          layout += new BorderPanel {
            layout += win.speedArea -> BorderPanel.Position.Center
            layout += win.speedLabel2 -> BorderPanel.Position.East
          } -> BorderPanel.Position.East
        }
        add(speedP, BorderPanel.Position.South)
        add(buttonP, BorderPanel.Position.East)
      }

      def loadSpitter()
        : (SpitterEngine, Iterator[String], Iterator[String]) = {
        val spitter =
          new SpitterEngine(cluster, win.outputArea, win.speedSlider)
        val (startIterator, usableIterator) =
          spitter.getTextFromEditor.toIterator.duplicate
        (spitter, startIterator, usableIterator)
      }

      def showOneDecimal(str: String): String = {
        val splitIt = str.split('.')
        splitIt(0) + "." + splitIt(1)(0)
      }

      def changeTextToSpeedSliderValue() = {
        val wps = (1.0 * win.speedSlider.value / 60).toString
        win.speedArea.text = showOneDecimal(wps)
      }

      def clickOnStartButton(): Unit = {
        if (win.startButton.text == "Start Spitting") {
          load = loadSpitter()
          spitter = load._1
          startIterator = load._2
          usableIterator = load._3
          spitter.stopIt = false
          win.startButton.text = "     Pause     "
          win.stopButton.peer.setEnabled(true)
          spitter.spit(usableIterator)
        } else if (win.startButton.text == "     Pause     ") {
          spitter.stopIt = true
          win.startButton.text = "   Continue   "
        } else if (win.startButton.text == "   Continue   ") {
          spitter.stopIt = false
          spitter.spit(usableIterator)
          win.startButton.text = "     Pause     "
        }
      }

      def clickOnStopButton(): Unit = {
        spitter.stopIt = true
        val newIterator = startIterator.duplicate
        startIterator = newIterator._1
        usableIterator = newIterator._2
        win.startButton.peer.setEnabled(true)
        win.startButton.text = "Start Spitting"
        win.stopButton.peer.setEnabled(false)
      }

      var load = loadSpitter()
      var spitter = load._1
      var startIterator = load._2
      var usableIterator = load._3

      changeTextToSpeedSliderValue()
      val colorMaker = new GeneralColoring(win.outputArea)
      if (cluster.vars.mode)
        colorMaker.theme = cluster.vars.theme
      else
        colorMaker.theme = "Eiffel"

      listenTo(win.startButton, win.stopButton, win.speedSlider)

      reactions += {
        case ButtonClicked(win.startButton) =>
          clickOnStartButton()
        case ButtonClicked(win.stopButton) =>
          clickOnStopButton()
        case ValueChanged(win.speedSlider) =>
          changeTextToSpeedSliderValue()
      }
    }
  }
}
