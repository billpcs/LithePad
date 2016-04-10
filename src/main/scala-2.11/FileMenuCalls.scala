import scala.swing.{MainFrame, TextField, EditorPane}
import FileChooserUtils._
import FutureLoading.futureOpen

class FileMenuCalls(currentPath: PathKeeper, editorPane: EditorPane,
                    infoShower: TextField, lineShower: TextField, top: MainFrame, cname: String) {

  def save() {
    currentPath.path = saveIfNotSaved()
    // If the user did not press cancel on the 'Save As' menu do:
    if (currentPath.path != "") {
      currentPath.setSaved()
      top.title = cname + currentPath
      infoShower.text = "File saved"
    }
  }

  def saveAs() {
    val ans = writeStringToPathAs(currentPath.path, editorPane.text)
    currentPath.path = if (ans.isDefined) ans.get else currentPath.path
    if (currentPath.path != "" && ans.isDefined) {
      currentPath.setSaved()
      top.title = cname + currentPath
      infoShower.text = "File saved"
    }
  }

  def saveIfNotSaved() : String  = {
    if (currentPath.path == "") writeStringToPathAs(stringToWrite = editorPane.text).get
    else {
      writeStringToPath(currentPath.path, editorPane.text)
      currentPath.path
    }
  }

  def newFile(){
    currentPath.setNotSaved()
    currentPath.path = ""
    top.title = cname
    editorPane.text = null
    editorPane.editable = true
    infoShower.text = "This is a brand new file"
  }

  def myOpen(globalVars: GlobalVars){
    val (path: Option[String], content: Option[String]) = chooseFileToOpen(globalVars)
    if (path.isDefined && content.isDefined) {
      currentPath.setSaved()
      currentPath.path = path.get
      futureOpen(content, editorPane, currentPath, infoShower, lineShower, top, cname)
    }else if (content.isEmpty) {
      infoShower.text = "Something went wrong. Could not open file"
    }else if (path.isEmpty) {
      infoShower.text = "You cancelled the open action"
    }
  }

}
