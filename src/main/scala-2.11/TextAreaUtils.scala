import jsyntaxpane.DefaultSyntaxKit

import scala.swing._
import NotePadMode.setEditorTheme

object TextAreaUtils {

  def changeFontsToSliderValue(editorPane: EditorPane, infoShower: TextField, fontSize: Int, fontStyle: String) = {
    editorPane.font = new Font(fontStyle, java.awt.Font.PLAIN,  fontSize)
    infoShower.text = s"Font size is set to $fontSize"
    fontSize
  }

  def goToEditorMode(editorPane: EditorPane, infoShower: TextField, globalVars: GlobalVars) = {
    val txt = editorPane.text
    val syntaxConfig = DefaultSyntaxKit.getConfig(classOf[DefaultSyntaxKit])
    syntaxConfig.put("DefaultFont", globalVars.font_style + " " + globalVars.font_size.toString)
    editorPane.contentType = "text/" + globalVars.available_syntaxes(0)
    editorPane.text = txt
    setEditorTheme(editorPane, infoShower, "Eiffel")
    infoShower.text = "You are now in the text editor mode"
  }

  def goToNotepadMode(editorPane: EditorPane, infoShower: TextField) = {
    val txt = editorPane.text
    editorPane.contentType = "text"
    setEditorTheme(editorPane, infoShower, "Lithe")
    editorPane.text = txt
    infoShower.text = "You are now in the notepad mode"
  }

}