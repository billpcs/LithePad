import javax.swing.UIManager
import javax.swing.undo.UndoManager

import SettingsParser._
import TextAreaUtils._
import NotePadMode._
import jsyntaxpane.DefaultSyntaxKit

import scala.swing._
import scala.swing.event._

object TextApp extends SimpleSwingApplication {

  override def main(args: Array[String]) = super.main(args)

  def top = new MainFrame {




    centerOnScreen()
    minimumSize = new Dimension(250, 250)
    resizable = true

    // A copy to pass it as a parameter in
    // functions. No better way to do it up to now
    val topFrameCopy = this

    // Global vars for various LithePad properties
    val globalVars = GlobalVars()

    // Global constants for various LithePad properties
    val globalConst = GlobalConst()

    // Title for the window
    title = globalConst.NAME

    // Create the currentPath instance
    val currentPath = PathKeeper("", globalVars)

    // Bottom line shower properties
    val infoShower = new TextField {
      font = new Font("Serif", java.awt.Font.BOLD, 12)
      columns = 10
      text = "Welcome! If this is your first time please read the Help."
      editable = false
    }


    // Text editor area properties
    val editorPane = new EditorPane() {
      DefaultSyntaxKit.initKit()
      preferredSize = new Dimension(650, 400)
      // All this will change , but they must exist if the config file could not be opened.
      font = new Font(globalVars.font_style, java.awt.Font.PLAIN, globalVars.font_size)
      //setEditorTheme(this, infoShower, "Monokai")
      setTabSize(this, 2)
      focusable = true
      resizable = true
      requestFocus()
    }



    // Create the undo and redo actions
    val undoManager = new UndoManager()
    editorPane.peer.getDocument.addUndoableEditListener(undoManager)


    // Shower for the line the cursor is placed
    val lineShower = new TextField {
      text = ""
      font = new Font("Serif", java.awt.Font.BOLD, 14)
      columns = 5
      editable = false
    }


    val fontSizeSlider = new Slider {
      min = globalConst.MIN_FONT_SIZE
      max = globalConst.MAX_FONT_SIZE
    }


    val tabSizeComboBox = new ComboBox[Int](1 to 8) {
      peer.setSelectedItem(globalConst.DEFAULT_TAB_SIZE)
      peer.setPreferredSize(new Dimension(50, 10))
      globalVars.last_tab_size_val = peer.getSelectedItem.toString
    }


    val tabSizeLabel = new Label() {
      font = new Font("Serif", java.awt.Font.PLAIN, 12)
      text = "Tab Size: "
    }


    try {
      UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName)
    }
    catch {
      case e: Exception => infoShower.text = "Error loading system Look and Feel. Using default."
    }


    // Get the settings from the .properties file and
    // give them to the global vars
    try {
      val settings = parseSettings(editorPane, infoShower, globalConst)
      globalVars.theme = settings._1
      globalVars.font_style = settings._2
      globalVars.font_size = settings._3
      fontSizeSlider.value = globalVars.font_size
      globalVars.tab_size = settings._4
      globalVars.max_file_size = settings._5
      globalVars.show_hidden_files = settings._6
      globalVars.correctValue = getCorrectDivideVal(editorPane)
      setTabSize(editorPane, globalVars.tab_size)
      tabSizeComboBox.peer.setSelectedItem(globalVars.tab_size)
    } catch {
      case _: Throwable => infoShower.text = "There must be an error in your settings file."
    }


    menuBar = new MenuBar {
      val menuBarCreator = new MenuBarCreator(currentPath, editorPane, infoShower, lineShower, globalConst, globalVars)
      contents += menuBarCreator.fileMenu(topFrameCopy)
      contents += menuBarCreator.editMenu(undoManager)
      contents += menuBarCreator.viewMenu()
      contents += menuBarCreator.toolsMenu()
      contents += menuBarCreator.preferencesMenu(fontSizeSlider)
      contents += menuBarCreator.helpMenu()
    }



    // Place the items in their place
    contents = new BorderPanel {
      add(new ScrollPane(editorPane), BorderPanel.Position.Center)
      val bp = new BoxPanel(Orientation.Horizontal)
      add(bp, BorderPanel.Position.South)
      bp.contents += lineShower
      bp.contents += tabSizeLabel
      bp.contents += tabSizeComboBox
      bp.contents += infoShower
    }

    // Add the listeners
    listenTo(editorPane.keys,
      editorPane.mouse.clicks,
      fontSizeSlider,
      tabSizeComboBox.selection
    )

    reactions += {
      case change: KeyPressed =>
        if (globalVars.isSaved || currentPath.path == "") {
          currentPath.setNotSaved()
          title = globalConst.NAME + currentPath
        }
      case r: KeyReleased =>
        lineShower.text = caretUpdate(editorPane)
      case m: MouseReleased =>
        lineShower.text = caretUpdate(editorPane)
      case ValueChanged(`fontSizeSlider`) =>
        globalVars.font_size = changeFontsToSliderValue(
              editorPane, infoShower, fontSizeSlider.value, globalVars.font_style
        )
        globalVars.correctValue = getCorrectDivideVal(editorPane)
      case SelectionChanged(`tabSizeComboBox`) =>
        val new_val = tabSizeComboBox.peer.getSelectedItem.toString
        if (globalVars.last_tab_size_val != new_val ) {
          changeTabSize(new_val, editorPane, infoShower)
          globalVars.last_tab_size_val = new_val
        }
    }


    pack()
  }
}