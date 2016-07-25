package textengine.messengers

class StartUpMessenger extends MessengerMethods {
  def info() = say("Created the bottom info pane")
  def editor() = say("Created the editor")
  def lines() = say("Created the line counter pane")
  def fontSlider() = say("Created the font slider")
  def tabSize() = say("Created the tab size combo box")
}
