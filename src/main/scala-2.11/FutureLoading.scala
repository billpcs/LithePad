import NotePadMode._
import scala.concurrent.Future
import scala.swing.{MainFrame, TextField, EditorPane}
import scala.concurrent.ExecutionContext.Implicits.global
import scala.util.{Failure, Success}


object FutureLoading {

  def futureOpen(content: Option[String],
                 editorPane: EditorPane,
                 currentPath: PathKeeper,
                 infoShower: TextField,
                 lineShower: TextField,
                 top: MainFrame,
                 cname: String) = {

    val paste = Future {
      editorPane.text = content.get
    }
    infoShower.text = "Loading file, please wait"
    paste.onComplete {
      case Success(s) =>
        top.title = cname + currentPath
        editorPane.peer.setCaretPosition(editorPane.peer.getDocument.getLength)
        lineShower.text = caretUpdate(editorPane)
        infoShower.text = "File is ready"
      case Failure(e) => editorPane.text = e.toString
    }
  }

  def futureSort(editorPane: EditorPane,
                 infoShower: TextField,
                 f:String => String,
                 duringMessage: String,
                 successMessage: String,
                 failureMessage: String) = {

    val res = Future {
      f(editorPane.text)
    }

    infoShower.text = duringMessage

    res.onComplete {
      case Success(sorted_file) =>
        editorPane.text = sorted_file
        infoShower.text = successMessage
      case Failure(e) => infoShower.text = failureMessage
    }
  }


}
