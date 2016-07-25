package textengine.messengers

class LoadMessenger {
  val readyMessage = "File is ready"
  val duringMessage = "File is being loaded"
  val failedMessage = "Could not load file"
  val fileNotFoundMessage = "File could not be found"
}

object LoadMessenger {
  def apply(): LoadMessenger = new LoadMessenger()
}