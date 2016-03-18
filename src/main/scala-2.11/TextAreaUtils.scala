import javax.swing.text.PlainDocument
import javax.swing.undo.UndoManager

import scala.swing._

object TextAreaUtils {

  def caretUpdate(editorPane: EditorPane): String = {
    try {
      val point = editorPane.peer.getCaret.getMagicCaretPosition
      val line = point.getY.toInt / getCorrectDivideVal(editorPane) + 1
      f"(Line: $line)"
    }
    catch {
      case e: NullPointerException => "Confused"
    }
  }

  def setTabSize(editorPane:EditorPane, n: Int) = {
    val d = editorPane.peer.getDocument
    if (d.isInstanceOf[PlainDocument]) {
      d.putProperty(PlainDocument.tabSizeAttribute, n)
    }
  }

  def changeTabSize(value: String, editorPane: EditorPane, infoShower: TextField) = {
    setTabSize(editorPane, value.toInt)
    infoShower.text = s"Changed tab size to $value."
    FileBufferUtils.reloadCurrentFile(editorPane)
  }

  // Get the correct value to divide, in order to show the correct line number
  def getCorrectDivideVal(editorPane: EditorPane): Int = {
    editorPane.peer.getFontMetrics(editorPane.peer.getFont).getHeight
  }

  def changeFontsToSliderValue(editorPane: EditorPane, infoShower: TextField, fontSize: Int, fontStyle: String) = {
    editorPane.font = new Font(fontStyle, java.awt.Font.PLAIN,  fontSize)
    infoShower.text = s"Font size is set to $fontSize"
    fontSize
  }

  def setEditorTheme(editorPane: TextComponent, infoShower: TextField, theme: String) = {
    val colorMaker = new GeneralColoring(editorPane)
    colorMaker.theme = theme
    editorPane.caret.color = editorPane.foreground
    infoShower.text = s"You updated your theme to $theme !"
  }

  def undoDone(undo: UndoManager, infoShower: TextField) = {
    if (undo.canUndo) {
      undo.undo()
      infoShower.text = "Undo done."
    }
    else infoShower.text = "Nothing to undo."
  }

  def redoDone(redo: UndoManager, infoShower: TextField) = {
    if (redo.canRedo) {
      redo.redo()
      infoShower.text = "Redo done."
    }
    else infoShower.text = "Nothing to redo."
  }

}