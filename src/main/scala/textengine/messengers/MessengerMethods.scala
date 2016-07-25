package textengine.messengers

class MessengerMethods {

  val prompt = "> "

  def say(s: String): Unit = {
    println(prompt + s)
  }

}
