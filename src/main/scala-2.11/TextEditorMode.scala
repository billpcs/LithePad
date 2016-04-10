import scala.swing.{EditorPane, TextArea, TextField}
import TextAreaUtils.goToEditorMode
object TextEditorMode {

  def changeSyntax(lang: String, editorPane: EditorPane, infoShower: TextField, globalVars: GlobalVars): Unit = {
    goToEditorMode(editorPane, infoShower, globalVars)
    val available_syntax = globalVars.available_syntaxes
    val txt = editorPane.text
    if (available_syntax contains lang) {
      editorPane.contentType = "text/"+lang
      editorPane.text = txt
      infoShower.text = s"You updated the syntax to $lang"
    } else {
      infoShower.text = s"Can not find syntax pack for $lang"
    }
  }

}
