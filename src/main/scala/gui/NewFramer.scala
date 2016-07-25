package gui

import scala.swing._
import scala.swing.event.ButtonClicked

object NewFramer {
  def fontSizeSliderFrame(fontSizeSlider: Slider) = {
    new Frame { font_size_slider_frame =>
      {
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
