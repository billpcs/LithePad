package textengine.messengers

class GeneralMessenger {
  val welcomeMessage = "Welcome! If this is your first time please read the Help"
  val lookAndFeelError = "Error loading system Look and Feel. Using default"
  val settingsFileError = "There must be an error in your settings file."
  val tabSizeMessage = "Tab Size: "
}

object GeneralMessenger {
  def apply(): GeneralMessenger = new GeneralMessenger()
}
