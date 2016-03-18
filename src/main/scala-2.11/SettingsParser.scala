import java.awt.Font
import java.io.FileNotFoundException
import java.util.Properties
import scala.swing.{EditorPane,TextField}
import TextAreaUtils.setEditorTheme
import scala.collection.JavaConversions._

object SettingsParser {

  def parseSettings(textArea: EditorPane, infoShower: TextField, globalConst: GlobalConst)
                                        :(String, String, Int, Int, Int) = {

    val properties = new Properties
    try {
      properties.load(TextApp.getClass.getResourceAsStream("/settings.properties"))
    } catch {
      case e : FileNotFoundException => null
    }

    def isSupportedFontSize(n:Int) : Boolean = {
      (globalConst.MIN_FONT_SIZE to globalConst.MAX_FONT_SIZE) contains n
    }

    def isSupportedTabSize(n: Int) : Boolean = {
      (1 to 8) contains n
    }

    def isSupportedFontStyle(n:String): Boolean = {
      val fontStyles = Array("Courier", "Monospaced")
      fontStyles.contains(n)
    }

    def isSupportedTheme(n:String) : Boolean = {
      val themes = Array("Cobalt" , "Eiffel" , "Monokai", "Hack", "Amy", "Cinnamon" , "Lithe")
      themes.contains(n)
    }

    var theme = globalConst.DEFAULT_THEME
    var font_style = globalConst.DEFAULT_FONT_STYLE
    var font_size = globalConst.DEFAULT_FONT_SIZE
    var file_size = globalConst.DEFAULT_MAX_FILE_SIZE
    var tab_size = globalConst.DEFAULT_TAB_SIZE
    var unsupported_setting = false
    for(key <- properties.stringPropertyNames()) {
      val value = properties.getProperty(key)
      key match {
        case "defaultTheme" =>
          if (isSupportedTheme(value)) theme = value
          else unsupported_setting = true
        case "fontStyle" =>
          if (isSupportedFontStyle(value)) font_style = value
          else unsupported_setting = true
        case "fontSize" =>
          if (isSupportedFontSize(value.toInt)) font_size = value.toInt
          else unsupported_setting = true
        case "tabSize" =>
          if (isSupportedTabSize(value.toInt)) tab_size = value.toInt
          else unsupported_setting = true
        case "maxFileSize" => file_size = value.toInt
        case _ => unsupported_setting = true
      }
    }

    setEditorTheme(textArea, infoShower , theme)
    textArea.font = new Font( font_style , java.awt.Font.PLAIN , font_size )
    if (unsupported_setting) {
      infoShower.text = "One or more of your settings are not supported."
    } else {
      infoShower.text = "All of your settings have been added successfully."
    }
    (theme , font_style , font_size, tab_size, file_size)
  }
}
