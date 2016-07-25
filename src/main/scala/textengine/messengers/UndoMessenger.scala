package textengine.messengers

class UndoMessenger {
  val doneUndo = "Undo done."
  val nothingToUndo = "Nothing to undo."
  val doneRedo = "Redo done."
  val nothingToRedo = "Nothing to redo."
}


object UndoMessenger {
  def apply(): UndoMessenger = new UndoMessenger()
}
