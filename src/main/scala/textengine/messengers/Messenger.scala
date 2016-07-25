package textengine.messengers

class Messenger(val loadMessenger: LoadMessenger,
                val sortMessenger: SortMessenger,
                val caretMessenger: CaretMessenger,
                val fileMessenger: FileMessenger,
                val generalMessenger: GeneralMessenger,
                val undoMessenger: UndoMessenger,
                val settingsMessenger: SettingsMessenger) {}

object Messenger {
  def apply(): Messenger =
    new Messenger(LoadMessenger(),
                  SortMessenger(),
                  CaretMessenger(),
                  FileMessenger(),
                  GeneralMessenger(),
                  UndoMessenger(),
                  SettingsMessenger())
}
