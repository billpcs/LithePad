package textengine.messengers

class FileMessenger {
  val fileSaved = "File saved successfully"
}

object FileMessenger {
  def apply(): FileMessenger = new FileMessenger()
}

