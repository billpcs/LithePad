package textengine

import javax.swing.undo.UndoManager
import global.{GlobalConst, GlobalVars}
import gui.PopUpMessages
import javax.swing.event.DocumentListener
import paths.PathKeeper

import scala.swing.{ComboBox, Dialog, EditorPane, MainFrame, Slider, TextField}

class Cluster(val top: MainFrame,
              val editor: EditorPane,
              val info: TextField,
              val pathKeeper: PathKeeper,
              val line: TextField,
              val fontSizeSlider: Slider,
              val tabSize: ComboBox[Int],
              val popUp: PopUpMessages,
              val undoManager: UndoManager,
              val docListener: DocumentListener,
              val windowName: String,
              val vars: GlobalVars,
              val const: GlobalConst)
