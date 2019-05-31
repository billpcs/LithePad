import javax.swing.{SwingUtilities, UIManager}
import javax.swing.text.PlainDocument
import javax.swing.undo.UndoManager
import gui._
import de.sciss.syntaxpane.DefaultSyntaxKit
import global.{GlobalConst, GlobalVars}
import javax.swing.event.{CaretEvent, CaretListener, DocumentEvent, DocumentListener}
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

  var cmdArgs: Array[String] = Array.empty[String]

  override def startup(args: Array[String]) {
    cmdArgs = args
    val t: MainFrame = top
    if (t.size == new Dimension(0, 0)) t.pack()
    t.visible = true
  }

  def top: MainFrame = new MainFrame {

    centerOnScreen()
    minimumSize = new Dimension(1, 1)
    resizable = true
    maximize()

    private val selff: MainFrame = this

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
    val infoShower: TextField = new TextField {
      font = new Font("Serif", java.awt.Font.BOLD, 12)
      columns = 10
      text = messenger.generalMessenger.welcomeMessage
      editable = false
    }

    // Text editor area properties
    val editorPane: EditorPane = new EditorPane() {
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
    val lineShower: TextField = new TextField {
      text = ""
      font = new Font("Serif", java.awt.Font.BOLD, 14)
      columns = 5
      editable = false
    }

    val fontSizeSlider: Slider = new Slider {
      min = const.MIN_FONT_SIZE
      max = const.MAX_FONT_SIZE
    }

    val tabSizeComboBox: ComboBox[Int] = new ComboBox[Int](1 to 8) {
      peer.setSelectedItem(const.DEFAULT_TAB_SIZE)
      peer.setPreferredSize(new Dimension(50, 10))
      vars.last_tab_size_val = peer.getSelectedItem.toString
    }

    val tabSizeLabel: Label = new Label() {
      font = new Font("Serif", java.awt.Font.PLAIN, 12)
      text = messenger.generalMessenger.tabSizeMessage
    }

    // Create the undo and redo actions
    val undoManager = new UndoManager()
    editorPane.peer.getDocument.addUndoableEditListener(undoManager)

    val undoHandler = new UndoHandler(undoManager, messenger, infoShower)

    val popUpMessages = new PopUpMessages(selff, currentPath)

    val docListener: DocumentListener = new DocumentListener {

      override def insertUpdate(documentEvent: DocumentEvent): Unit =
        this.changedUpdate(documentEvent)

      override def removeUpdate(documentEvent: DocumentEvent): Unit =
        this.changedUpdate(documentEvent)

      override def changedUpdate(documentEvent: DocumentEvent): Unit = {
        // if file is changed, update various variables
        if (cluster.vars.fileIsSaved || cluster.pathKeeper.path == "") {
          cluster.pathKeeper.isnew = false
          cluster.pathKeeper.saved = false
          cluster.top.title = cluster.const.NAME + cluster.pathKeeper
        }
      }
    }

    val cluster = new Cluster(this,
                              editorPane,
                              infoShower,
                              currentPath,
                              lineShower,
                              fontSizeSlider,
                              tabSizeComboBox,
                              popUpMessages,
                              undoManager,
                              docListener,
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
      case _: Exception =>
        cluster.info.text = messenger.generalMessenger.lookAndFeelError
    }

    try {
      parseSettings(cluster, messenger)
    } catch {
      case _: Throwable =>
        infoShower.text = messenger.generalMessenger.settingsFileError
    }

    // Create the menu bar, together with their actions
    menuBar = new MenuBar {
      val menuBarCreator = new MenuBarCreator(cluster, textEngine, undoHandler)
      contents += menuBarCreator.fileMenu(selff)
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

    cluster.editor.peer.getDocument.addDocumentListener(cluster.docListener)

    pack()

    // Must be the last thing to do, after everything is created
    TerminalOptions.parse(cmdArgs, cluster, contentUtils)

  }
}
