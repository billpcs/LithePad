import scala.swing._
import scala.swing.event.ButtonClicked

object NewFramer {
  def fontSizeSliderFrame(fontSizeSlider: Slider) = {
    new Frame {
      font_size_slider_frame => {
        title = "Font Size"
        visible = true
        resizable = false
        peer.setAlwaysOnTop(true)
        // Place it in the centre
        peer.setLocationRelativeTo(null)
        contents = new FlowPanel {
          contents += fontSizeSlider
          contents += new Button(Action("Done") {
            font_size_slider_frame.dispose()
          })
        }
      }
    }
  }
}


class ListButtons(val names: Array[String], val functions: Array[()=>Unit])


class MessageBox(windowTitle: String, windowMessage: Option[String], buttonsO: Option[ListButtons]) {

  def mainFrame() = new Frame {
    title = windowTitle
    visible = true
    resizable = true
    peer.setAlwaysOnTop(true)
    centerOnScreen()
    size = new Dimension(200, 200)


    contents = new FlowPanel {
      if(windowMessage.isDefined)
        contents += new TextArea() {
          text = windowMessage.toString
          editable = false
        }


      if(buttonsO.isDefined) {
        val buttons = buttonsO.get

        // Create button instances out of the names
        val buttonArray = buttons.names.map(new Button(_))

        // Add the listeners for each one
        buttonArray.foreach(listenTo(_))

        // Change the names of the buttons
        (1 until buttonArray.length).foreach { i =>
          buttonArray(i).text = buttons.names(i)
        }

        // Add the buttons to the frame
        buttonArray.foreach(contents += _)

        // Create the reactions
        reactions += {
          case b: ButtonClicked =>
            for(button <- buttonArray) {
              if (b.source == button) {
                buttons.functions(buttons.names.indexOf(button.text))() // <- don't forget ()
              }
            }
        }
      }

    }

  }
}
