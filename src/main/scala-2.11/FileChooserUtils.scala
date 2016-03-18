import java.io._
import javax.swing.UIManager
import scala.swing.FileChooser
import FileBufferUtils._

object FileChooserUtils {
  def writeStringToPathAs(title: String = "", stringToWrite : String) : Option[String] = {
    UIManager.put("FileChooser.readOnly", true)
    val chooser = new FileChooser(null)
    val result = chooser.showSaveDialog(null)
    chooser.title = "Saving your file as ..."
    // Have to add checks so that you don't overwrite
    // a file without wanting to
    if (result == FileChooser.Result.Approve) {
      val pathSelected = chooser.selectedFile.toString
      writeStringToPath(pathSelected, stringToWrite)
      Some(pathSelected)
    }else Some(title)
  }

  def writeStringToPath(path : String, stringToWrite:String) {
    writeToFile(new File(path + ""), stringToWrite.split("\n"))
  }

  def chooseFileToOpen() : (Option[String] , Option[String]) = {
    UIManager.put("FileChooser.readOnly", true)
    val chooser = new FileChooser(null)
    val result = chooser.showOpenDialog(null)
    chooser.title = "What would you like to open?"
    if (result == FileChooser.Result.Approve){
      val pathSelected = chooser.selectedFile.toString
      (Some(pathSelected) , getContent(pathSelected))
    }else (None, Some(""))
  }

}



