import javax.swing.UIManager
import javax.swing.text.PlainDocument
import javax.swing.undo.UndoManager

import gui._
import de.sciss.syntaxpane.DefaultSyntaxKit
import global.{GlobalConst, GlobalVars}
import paths.PathKeeper
import textengine._
import textengine.messengers._
import textengine.settings.SettingsParser.parseSettings
import terminaloptions.TerminalOptions
import textengine.reactors.{FontSizeSliderReactor, KeyReactor, TabSizeComboBoxReactor}
import textengine.trackers.KeyPressTracker

import scala.swing._
import scala.swing.event._

object LithePad extends SimpleSwingApplication {

  override def main(args: Array[String]) = super.main(args)

  override def startup(args: Array[String]) {
    val f = new java.io.PrintWriter(".lithepad", "UTF-8")
    if (args.length > 0) {
      f.println(args.mkString(" "))
    } else {
      f.println("Lithe.NoArguments")
    }
    f.close()
    val t = top
    if (t.size == new Dimension(0, 0)) t.pack()
    t.visible = true
  }

  def top = new MainFrame {

    centerOnScreen()
    minimumSize = new Dimension(250, 250)
    resizable = true
    maximize()

    // A copy to pass it as a parameter in
    // functions. No better way to do it up to now
    val topFrameCopy = this

    // Global vars for various LithePad properties
    val vars = GlobalVars()

    // Global constants for various LithePad properties
    val const = GlobalConst()

    // Title for the window
    title = const.NAME

    // Create the currentPath instance
    val currentPath = PathKeeper("", vars)

    val messenger = Messenger()

    // Bottom line shower properties
    val infoShower = new TextField {
      font = new Font("Serif", java.awt.Font.BOLD, 12)
      columns = 10
      text = messenger.generalMessenger.welcomeMessage
      editable = false
    }

    // Text editor area properties
    val editorPane = new EditorPane() {
      DefaultSyntaxKit.initKit()
      preferredSize = new Dimension(650, 400)
      // All this will change , but they must exist if the config file could not be opened.
      font = new Font(vars.font_style, java.awt.Font.PLAIN, vars.font_size)
      peer.getDocument.putProperty(PlainDocument.tabSizeAttribute, 2)
      focusable = true
      resizable = true
      requestFocus()
    }

    new Button()

    // Shower for the line the cursor is placed
    val lineShower = new TextField {
      text = ""
      font = new Font("Serif", java.awt.Font.BOLD, 14)
      columns = 5
      editable = false
    }

    val fontSizeSlider = new Slider {
      min = const.MIN_FONT_SIZE
      max = const.MAX_FONT_SIZE
    }

    val tabSizeComboBox = new ComboBox[Int](1 to 8) {
      peer.setSelectedItem(const.DEFAULT_TAB_SIZE)
      peer.setPreferredSize(new Dimension(50, 10))
      vars.last_tab_size_val = peer.getSelectedItem.toString
    }

    val tabSizeLabel = new Label() {
      font = new Font("Serif", java.awt.Font.PLAIN, 12)
      text = messenger.generalMessenger.tabSizeMessage
    }

    // Create the undo and redo actions
    val undoManager = new UndoManager()
    editorPane.peer.getDocument.addUndoableEditListener(undoManager)

    val undoHandler = new UndoHandler(undoManager, messenger, infoShower)

    val popUpMessages = new PopUpMessages(topFrameCopy, currentPath)

    val cluster = new Cluster(this,
                              editorPane,
                              infoShower,
                              currentPath,
                              lineShower,
                              fontSizeSlider,
                              tabSizeComboBox,
                              popUpMessages,
                              undoManager,
                              title,
                              vars,
                              const)
    val textUtils = new TextUtils(cluster, messenger)
    val contentUtils = new ContentUtils(cluster, messenger, textUtils)
    val fileUtils = new FileUtils(cluster, contentUtils, messenger)
    val textEngine = new TextEngine(contentUtils, fileUtils, textUtils)

    try {
      UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName)
    } catch {
      case e: Exception =>
        cluster.info.text = messenger.generalMessenger.lookAndFeelError
    }

    try {
      parseSettings(cluster, messenger)
    } catch {
      case t: Throwable =>
        infoShower.text = messenger.generalMessenger.settingsFileError
    }

    // Create the menu bar, together with their actions
    menuBar = new MenuBar {
      val menuBarCreator = new MenuBarCreator(cluster, textEngine, undoHandler)
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
             tabSizeComboBox.selection)

    // Create our handmade reactors
    val keyPressTracker = new KeyPressTracker
    val keyReactor = new KeyReactor(cluster, keyPressTracker, textUtils)
    val fontSizeSliderReactor = new FontSizeSliderReactor(cluster)
    val tabSizeComboBoxReactor = new TabSizeComboBoxReactor(cluster, textUtils)

    reactions += {
      case change: KeyPressed =>
        keyReactor.keyPressed(change)
      case change: KeyReleased =>
        keyReactor.keyReleased(change)
      case change: MouseReleased =>
        lineShower.text = textUtils.caretUpdate
      case ValueChanged(`fontSizeSlider`) =>
        fontSizeSliderReactor.valueChangedReact()
      case SelectionChanged(`tabSizeComboBox`) =>
        tabSizeComboBoxReactor.selectionChangedReact()
    }
    pack()

    // Must be the last thing to do, after everything is created
    val args = io.Source
      .fromFile(".lithepad")
      .getLines
      .flatMap(_.split(" ").map(_.trim))
      .toArray
    if (!(args contains "Lithe.NoArguments"))
      TerminalOptions.parse(args, cluster, contentUtils)

  }
}
