package textengine.messengers

class CaretMessenger {
  val nullMessage = "Confused"
}

object CaretMessenger {
  def apply(): CaretMessenger = new CaretMessenger
}