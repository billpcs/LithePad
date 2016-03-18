import java.awt.Color

import scala.swing.TextComponent

class GeneralColoring(pane: TextComponent) {
  var th = "Lithe"

  def theme = th

  def theme_=(theme: String) = {
    theme match {
      case "Amy" =>
        th = "Amy"
        pane.background = new Color(32, 0, 32)
        pane.foreground = new Color(127, 179, 225)
      case "Cobalt" =>
        th = "Cobalt"
        pane.background = new Color(0, 77, 101)
        pane.foreground = Color.white
      case "Monokai" =>
        th = "Monokai"
        pane.background = new Color(39, 40, 34)
        pane.foreground = Color.white
      case "Eiffel" =>
        th = "Eiffel"
        pane.background = Color.white
        pane.foreground = Color.black
      case "Hack" =>
        th = "Hack"
        pane.background = Color.black
        pane.foreground = Color.green
      case "Cinnamon" =>
        th = "Cinnamon"
        pane.background = new Color(42, 33, 28)
        pane.foreground = new Color(189, 174, 157)
      case "Lithe" =>
        th = "Lithe"
        pane.background = new Color(31, 35, 34)
        pane.foreground = new Color(46, 201, 174)
      case _ =>
        th = "Lithe"
        pane.background = new Color(31, 35, 34)
        pane.foreground = new Color(46, 201, 174)
    }
  }
}
