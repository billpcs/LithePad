import java.io.{FileInputStream, BufferedInputStream, File}

import scala.io.Source
import scala.io.Source._
import scala.swing.EditorPane

object FileBufferUtils {

  def getCurrentDirectory = new File(".").getCanonicalPath

  def numOfLines(fileInString: String) : Int = {
    fileInString.count(x => x == '\n') + 1
  }

  def externalLineCount(pathToFile: String) : Int = {
    fromFile(pathToFile).getLines().length
  }

  def writeToFile(f: java.io.File, data: Array[String]) {
    val p = new java.io.PrintWriter(f)
    data.foreach(p.println)
    p.close()
  }

  def getContent(pathToFile : String): Option[String] = {
    val in = new BufferedInputStream(new FileInputStream(pathToFile))
    val numBytes = in.available()
    val bytes = new Array[Byte](numBytes)
    if (bytes.length < Int.MaxValue) {
      in.read(bytes, 0, numBytes)
      Some(new String(bytes, "UTF-8"))
    }
    else None
  }

  /*
  We need this version to access files IN the jar
   */
  def getFileInJar(relativePath: String) : String = {
    val inp = Source.fromURL(getClass.getResource(relativePath))("UTF-8")
    val data = inp.getLines().mkString("\n")
    inp.close()
    data
  }

  def getHelp = getFileInJar("/README.txt")

  def getSettingsFile = getFileInJar("/settings.properties")

  def getChangelog = getFileInJar("/CHANGELOG")

  def getSettingsPath = getClass.getResource("/settings.properties").getPath

  def reloadCurrentFile(textArea: EditorPane){
    val data = textArea.text
    textArea.text = data
  }

  def sortFileInLines(fileInString: String): String = {
    fileInString.split("\n").sorted.mkString("\n")
  }

  def sortFileInWords(fileInString: String): String = {
    fileInString.split(" ").filter(_.trim != "").sorted.mkString("\n")
  }
}