package textengine

import javax.swing.undo.UndoManager

import textengine.messengers.Messenger

import scala.swing.TextField

class UndoHandler(action: UndoManager, messenger: Messenger, info: TextField) {

  def undoDone() = {
    if (action.canUndo) {
      action.undo()
      info.text = messenger.undoMessenger.doneUndo
    }
    else
      info.text = messenger.undoMessenger.nothingToUndo
  }

  def redoDone() = {
    if (action.canRedo) {
      action.redo()
      info.text = messenger.undoMessenger.doneRedo
    }
    else info.text = messenger.undoMessenger.nothingToRedo
  }


}
