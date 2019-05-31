package textengine

import javax.swing.text.PlainDocument

import textengine.messengers.Messenger
import scala.swing.EditorPane

class TextUtils(cluster: Cluster, messenger: Messenger) {

  /*
    Updates the line display to the current position of the caret
  */
  def caretUpdate: String = {
    try {
      val point = cluster.editor.peer.getCaret.getMagicCaretPosition
      val line = point.getY.toInt / getCorrectDivideVal + 1
      f"(Line: $line)"
    }
    catch {
      case _: NullPointerException => messenger.caretMessenger.nullMessage
    }
  }


  /*
    Gets the correct value to divide, in order to show the correct line number
   */
  def getCorrectDivideVal: Int = {
    cluster.editor.peer.getFontMetrics(cluster.editor.peer.getFont).getHeight
  }


  /*
    Returns the number of new line characters in a string
  */
  def numOfLines(fileInString : String) : Int = {
    fileInString.count(x => x == '\n') + 1
  }

  /*
    Given a string it converts it to title case
  */
  def toTitleCase(str:String): String = {
    def toTitleCaseWord(s: String): String = s.take(1).toUpperCase + s.drop(1)
    str.split(" ").map(toTitleCaseWord).mkString(" ")
  }



  def setTabSize(editorPane:EditorPane, n: Int): Unit = {
    val d = editorPane.peer.getDocument
    if (d.isInstanceOf[PlainDocument]) {
      editorPane.peer.getDocument.putProperty(PlainDocument.tabSizeAttribute, n)
    }
  }

  def changeTabSize(value: String): Unit = {
    setTabSize(cluster.editor, value.toInt)
    cluster.info.text = s"Changed tab size to $value."
    // WARNING, MUST USE reloadContent from ContentUtils, fix later
    val data = cluster.editor.text
    cluster.editor.text = data
  }


}