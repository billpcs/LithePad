package textengine

import java.io.File
import javax.swing.UIManager

import textengine.messengers.Messenger

import scala.io.Source
import scala.swing.FileChooser

class FileUtils(cluster: Cluster,
                contentUtils: ContentUtils,
                messenger: Messenger) {

  /*
    Given a path as a string, it opens the file with UTF
    (for use with small files, belonging to LithePad)
   */
  def getFileWithUTF8(relativePath: String): String = {
    Source
      .fromURL(getClass.getResource(relativePath))("UTF-8")
      .getLines()
      .mkString("\n")
  }

  def getHelp: String = getFileWithUTF8("/README.txt")

  def getSettingsFile: String = getFileWithUTF8("/settings.properties")

  def getSettingsPath: String = getClass.getResource("/settings.properties").getPath

  def getChangeLog: String = getFileWithUTF8("/CHANGELOG")

  def writeToFile(f: File, data: String) {
    val p = new java.io.PrintWriter(f)
    p.print(data)
    p.close()
  }

  def saveAsWindow(string: String): Option[String] = {
    cluster.editor.editable = true
    UIManager.put("FileChooser.readOnly", true)
    val chooser = new FileChooser(null)
    val result = chooser.showSaveDialog(null)
    chooser.title = "Saving your file as ..."
    if (result == FileChooser.Result.Approve) {
      val pathSelected = chooser.selectedFile
      writeStringToPath(pathSelected, string)
      Some(pathSelected.toString)
    } else
      Some(cluster.pathKeeper.path)
  }

  def openFileWindow(): Option[String] = {
    cluster.editor.editable = true
    UIManager.put("FileChooser.readOnly", true)
    val chooser = new FileChooser(null)
    chooser.title = "What would you like to open?"
    if (cluster.vars.show_hidden_files)
      chooser.fileHidingEnabled = false
    val result = chooser.showOpenDialog(null)
    if (result == FileChooser.Result.Approve)
      Some(chooser.selectedFile.toString)
    else
      None
  }

  def writeStringToPath(path: File, fileInString: String) {
    writeToFile(path, fileInString)
  }

  def needSave(currentPath: String, txt: String): String = {
    if (currentPath == "")
      saveAsWindow(txt).get
    else {
      writeStringToPath(new File(currentPath), txt)
      currentPath
    }
  }

  def saveAction() {
    cluster.editor.editable = true
    cluster.pathKeeper.path =
      needSave(cluster.pathKeeper.path, cluster.editor.text)
    // If the user did not press cancel on the 'Save As' menu do:
    if (cluster.pathKeeper.path != "") {
      cluster.pathKeeper.saved = true
      cluster.top.title = cluster.windowName + cluster.pathKeeper
      cluster.info.text = messenger.fileMessenger.fileSaved
    }

  }

  def saveAsAction() {
    cluster.editor.editable = true
    cluster.pathKeeper.path = saveAsWindow(cluster.editor.text).get
    if (cluster.pathKeeper.path != "") {
      cluster.pathKeeper.saved = true
      cluster.top.title = cluster.windowName + cluster.pathKeeper
      cluster.info.text = messenger.fileMessenger.fileSaved
    }
  }

  def saveIfNotSaved(): String = {
    cluster.editor.editable = true
    if (cluster.pathKeeper.path == "")
      saveAsWindow(string = cluster.editor.text).get
    else {
      saveAsWindow(cluster.editor.text)
      cluster.pathKeeper.path
    }
  }

  def newFileAction() {
    if (cluster.popUp.wantToCloseFile()) {
      cluster.pathKeeper.saved = false
      cluster.pathKeeper.isnew = true
      cluster.pathKeeper.path = ""
      cluster.top.title = cluster.windowName
      cluster.editor.text = null
      cluster.editor.editable = true
      cluster.undoManager.discardAllEdits()
      cluster.info.text = "This is a brand new file"
    }
  }

  def openAction() {
    if (cluster.popUp.wantToCloseFile()) {
      cluster.editor.editable = true
      val path: Option[String] = openFileWindow()
      if (path.isDefined) {
        contentUtils.loadContentFromPath(path.get)
        cluster.undoManager.discardAllEdits()
        cluster.pathKeeper.saved = true
        cluster.pathKeeper.path = path.get
      } else if (path.isEmpty) {
        cluster.info.text = "You cancelled the open action"
      }
    }
  }
}
