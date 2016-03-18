import java.io._
import javax.swing.UIManager
import scala.io.Source
import scala.io.Source._
import scala.swing.{MainFrame, TextField, EditorPane, FileChooser}

object UtilityMethods {

  def getCurrentDirectory = new File(".").getCanonicalPath

  def numOfLines( fileInString : String ) : Int = {
    fileInString.count(x => x == '\n') + 1
  }

  def externalLineCount( pathToFile : String ) : Int = {
    fromFile(pathToFile).getLines().length
  }

  def writeToFile(f: java.io.File, data: Array[String]) {
    val p = new java.io.PrintWriter(f)
    data.foreach(p.println)
    p.close()
  }

  def getContent(pathToFile : String) : String =
    fromFile(pathToFile).getLines().mkString("\n")

  def reloadCurrentFile(textArea: EditorPane){
    val data = textArea.text
    textArea.text = data
  }

  def getStringAmongTwoStrings(str: String, start: String = "=", end: String = ";") : String =
    str.split(start).last.split(end)(0).trim

  def needSave( currentPath: String , txt : String) : String  = {
    if (currentPath == "") writeStringToPathAs("", txt)
    else {
      writeStringToPath(currentPath, txt)
      currentPath
    }
  }

  def writeStringToPathAs( title: String = "" , fileInString : String ) : String = {
    val chooser = new FileChooser(null)
    UIManager.put("FileChooser.readOnly", true)
    chooser.title = "Saving your file as ..."
    val result = chooser.showSaveDialog(null)
    if (result == FileChooser.Result.Approve) {
      val pathSelected = chooser.selectedFile.toString
      writeStringToPath(pathSelected, fileInString)
      pathSelected
    }else title
  }

  def writeStringToPath(path : String, fileInString : String) {
    writeToFile(new File(path + ""), fileInString.split("\n"))
  }

  def saveAs(currentPath: PathKeeper, infoShower: TextField, top: MainFrame, paneText:String, cname: String) {
    currentPath.path = writeStringToPathAs(currentPath.path, paneText)
    if (currentPath.path != "") {
      currentPath.setSaved()
      top.title = cname + currentPath
      infoShower.text = "File saved."
    }
  }

  def save(currentPath: PathKeeper, infoShower: TextField, top: MainFrame, paneText: String, cname: String) {
    currentPath.path = needSave(currentPath.path, paneText)
    // If the user did not press cancel on the 'Save As' menu do:
    if (currentPath.path != "") {
      currentPath.setSaved()
      top.title = cname + currentPath
      infoShower.text = "File saved."
    }
  }

  def mopen(currentPath: PathKeeper, editorPane: EditorPane, infoShower: TextField, top: MainFrame, cname: String){
    val (path: String, contents: String) = chooseFileToOpen
    // If we got a path, other than an empty string do:
    if (path != "") {
      currentPath.setSaved()
      currentPath.path = path
      editorPane.text = contents
      editorPane.peer.setCaretPosition(editorPane.peer.getDocument.getLength)
      infoShower.text = f"This file has ${numOfLines(editorPane.text)} lines"
      top.title = cname + currentPath
    }
  }

  def newFile(currentPath: PathKeeper, editorPane: EditorPane, infoShower: TextField, top: MainFrame, cname: String){
    currentPath.setNotSaved()
    currentPath.path = ""
    top.title = cname
    editorPane.text = null
    editorPane.editable = true
    infoShower.text = "This is a brand new file."
  }

  def chooseFileToOpen : ( String , String ) = {
    val chooser = new FileChooser(null)
    UIManager.put("FileChooser.readOnly", true)
    chooser.title = "What would you like to open?"
    val result = chooser.showOpenDialog(null)
    if (result == FileChooser.Result.Approve){
      val pathSelected = chooser.selectedFile.toString
      chooser.title = pathSelected+"-"+""
      (pathSelected , getContent(pathSelected))
    }else ("", "")
  }

  def sortFile(fileInString:String) : String = {
    fileInString.split("\n").sorted.mkString("\n")
  }

  def toTitleCase(s:String) = s(0).toUpper + s.substring(1).toLowerCase

  def getFileWithUTF8(relativePath: String) : String = {
    val inp =  Source.fromURL(getClass.getResource(relativePath))("UTF-8")
    val ret = inp.getLines().mkString("\n")
    inp.close() ; ret ;
  }

  def getHelp = getFileWithUTF8("/README.txt")

  def getSettingsFile = getFileWithUTF8("/settings.properties")

  def getSettingsPath = getClass.getResource("/settings.properties").getPath

}
