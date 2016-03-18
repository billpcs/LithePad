import javax.swing.undo.UndoManager
import TextAreaUtils._
import scala.swing._
import javax.swing.KeyStroke
import java.awt.event.InputEvent
import NewFramer._
import FutureLoading._

class MenuBarCreator(currentPath: PathKeeper,
                     editorPane: EditorPane,
                     infoShower: TextField,
                     lineShower: TextField,
                     globalConst: GlobalConst,
                     globalVars: GlobalVars) {


  def fileMenu(top: MainFrame) = new Menu("File") {
    val fileMenuCall = new FileMenuCalls(currentPath, editorPane, infoShower, lineShower, top, globalConst.NAME)
    contents += new MenuItem(new Action("New File") {
      accelerator = Some(KeyStroke.getKeyStroke('N', InputEvent.CTRL_DOWN_MASK))
      def apply() {
        fileMenuCall.newFile()
      }
    })
    contents += new MenuItem(new Action("Open File") {
      accelerator = Some(KeyStroke.getKeyStroke('O', InputEvent.CTRL_DOWN_MASK))
      def apply() {
        fileMenuCall.myOpen()
      }
    })
    contents += new MenuItem(new Action("Save") {
      accelerator = Some(KeyStroke.getKeyStroke('S', InputEvent.CTRL_DOWN_MASK))
      def apply() {
        fileMenuCall.save()
      }
    })
    contents += new MenuItem(new Action("Save as") {
      accelerator = Some(KeyStroke.getKeyStroke('S', InputEvent.CTRL_DOWN_MASK | InputEvent.SHIFT_DOWN_MASK))
      def apply() {
        fileMenuCall.saveAs()
      }
    })
    contents += new MenuItem(new Action("Quit") {
      accelerator = Some(KeyStroke.getKeyStroke('Q', InputEvent.CTRL_DOWN_MASK))
      def apply() {
        sys.exit(0)
      }
    })
  }

  def editMenu(undoManager: UndoManager) = new Menu("Edit") {
    contents += new MenuItem(new Action("Copy") {
      accelerator = Some(KeyStroke.getKeyStroke('C', InputEvent.CTRL_DOWN_MASK))
      def apply() {
        editorPane.copy()
      }
    })
    contents += new MenuItem(new Action("Paste") {
      accelerator = Some(KeyStroke.getKeyStroke('V', InputEvent.CTRL_DOWN_MASK))
      def apply() {
        editorPane.paste()
      }
    })
    contents += new MenuItem(new Action("Cut") {
      accelerator = Some(KeyStroke.getKeyStroke('X', InputEvent.CTRL_DOWN_MASK))
      def apply() {
        editorPane.cut()
      }
    })
    contents += new MenuItem(new Action("Select All") {
      accelerator = Some(KeyStroke.getKeyStroke('A', InputEvent.CTRL_DOWN_MASK))
      def apply() {
        editorPane.selectAll()
      }
    })
    contents += new MenuItem(new Action("Undo") {
      accelerator = Some(KeyStroke.getKeyStroke('Z', InputEvent.CTRL_DOWN_MASK))
      def apply() {
        undoDone(undoManager, infoShower)
      }
    })
    contents += new MenuItem(new Action("Redo") {
      accelerator = Some(KeyStroke.getKeyStroke('Y', InputEvent.CTRL_DOWN_MASK))
      def apply() {
        redoDone(undoManager, infoShower)
      }
    })
    contents += new Menu("Text Manipulation") {
      contents += new MenuItem(Action("Sort Lines"){
        futureSort(editorPane, infoShower, FileBufferUtils.sortFileInLines,
          "Sorting lines, please wait", "Done sorting", "Failed to sort")
      })
      contents += new MenuItem(Action("Sort Words"){
        futureSort(editorPane, infoShower, FileBufferUtils.sortFileInWords,
          "Sorting words, please wait", "Done sorting", "Failed to sort")
      })

    }
  }

  def viewMenu() = new Menu("View") {
    contents += new Menu("Spitter"){
      contents += new MenuItem(Action("Run Spitter"){
        new SpitterWindow(editorPane, globalVars)
      })
    }
  }

  def preferencesMenu(fontSizeSlider: Slider) = new Menu("Preferences") {
    contents += new Menu("Themes") {
      contents += new MenuItem(Action("Amy") {
        setEditorTheme(editorPane, infoShower, "Amy")
      })
      contents += new MenuItem(Action("Cobalt") {
        setEditorTheme(editorPane, infoShower, "Cobalt")
      })
      contents += new MenuItem(Action("Eiffel") {
        setEditorTheme(editorPane, infoShower, "Eiffel")
      })
      contents += new MenuItem(Action("Hack") {
        setEditorTheme(editorPane, infoShower, "Hack")
      })
      contents += new MenuItem(Action("Monokai") {
        setEditorTheme(editorPane, infoShower, "Monokai")
      })
      contents += new MenuItem(Action("Cinnamon") {
        setEditorTheme(editorPane, infoShower, "Cinnamon")
      })
      contents += new MenuItem(Action("Lithe") {
        setEditorTheme(editorPane, infoShower, "Lithe")
      })
    }
    contents += new MenuItem(Action("Font Size") {
      fontSizeSliderFrame(fontSizeSlider)
      // Update the slider value, every time the user goes to see it.
      fontSizeSlider.value = globalVars.font_size
    })

    contents += new MenuItem(Action("View Default Settings") {
      editorPane.editable = true
      editorPane.text = FileBufferUtils.getSettingsFile
      currentPath.path = FileBufferUtils.getSettingsPath
    })
  }

  def helpMenu() = new Menu("Help") {
    contents += new MenuItem(Action("Show Help") {
      infoShower.text = "Is this what you were looking for?"
      editorPane.text = FileBufferUtils.getHelp
      editorPane.editable = false
    })
    contents += new MenuItem(Action("Show Changelog"){
      infoShower.text = "You are now looking at the changelog"
      editorPane.text = FileBufferUtils.getChangelog
      editorPane.editable = false
    })
  }


}
