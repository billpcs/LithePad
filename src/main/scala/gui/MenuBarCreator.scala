package gui

import java.awt.event.InputEvent
import javax.swing.KeyStroke
import javax.swing.undo.UndoManager

import gui.ChangeModes._
import gui.NewFramer.fontSizeSliderFrame
import colors.ThemeSettings._
import textengine.{Cluster, TextEngine, UndoHandler}

import scala.swing._

class MenuBarCreator(cluster: Cluster,
                     litheUtils: TextEngine,
                     undoHandler: UndoHandler) {
  val syntaxChooser: ComboBox[String] =
    new ComboBox[String](cluster.const.AVAILABLE_SYNTAX.values.toSeq) {
      peer.setSelectedItem(cluster.const.AVAILABLE_SYNTAX("Scala"))
    }

  def fileMenu(top: MainFrame): Menu = new Menu("File") {
    contents += new MenuItem(new Action("New File") {
      accelerator =
        Some(KeyStroke.getKeyStroke('N', InputEvent.CTRL_DOWN_MASK))
      def apply() {
        litheUtils.fileUtils.newFileAction()
      }
    })
    contents += new MenuItem(new Action("Open File") {
      accelerator =
        Some(KeyStroke.getKeyStroke('O', InputEvent.CTRL_DOWN_MASK))
      def apply() {
        litheUtils.fileUtils.openAction()
      }
    })
    contents += new MenuItem(new Action("Save") {
      accelerator =
        Some(KeyStroke.getKeyStroke('S', InputEvent.CTRL_DOWN_MASK))
      def apply() {
        litheUtils.fileUtils.saveAction()
      }
    })
    contents += new MenuItem(new Action("Save as") {
      accelerator = Some(
          KeyStroke.getKeyStroke(
              'S',
              InputEvent.CTRL_DOWN_MASK | InputEvent.SHIFT_DOWN_MASK))
      def apply() {
        litheUtils.fileUtils.saveAsAction()
      }
    })
    contents += new MenuItem(new Action("Quit") {
      accelerator =
        Some(KeyStroke.getKeyStroke('Q', InputEvent.CTRL_DOWN_MASK))
      def apply() {
        if (cluster.popUp.wantToQuit()) sys.exit(0)
      }
    })
  }

  def editMenu(undoManager: UndoManager): Menu = new Menu("Edit") {
    contents += new MenuItem(new Action("Copy") {
      accelerator =
        Some(KeyStroke.getKeyStroke('C', InputEvent.CTRL_DOWN_MASK))
      def apply() {
        cluster.editor.copy()
      }
    })
    contents += new MenuItem(new Action("Paste") {
      accelerator =
        Some(KeyStroke.getKeyStroke('V', InputEvent.CTRL_DOWN_MASK))
      def apply() {
        cluster.editor.paste()
      }
    })
    contents += new MenuItem(new Action("Cut") {
      accelerator =
        Some(KeyStroke.getKeyStroke('X', InputEvent.CTRL_DOWN_MASK))
      def apply() {
        cluster.editor.cut()
      }
    })
    contents += new MenuItem(new Action("Select All") {
      accelerator =
        Some(KeyStroke.getKeyStroke('A', InputEvent.CTRL_DOWN_MASK))
      def apply() {
        cluster.editor.selectAll()
      }
    })
    contents += new MenuItem(new Action("Undo") {
      accelerator =
        Some(KeyStroke.getKeyStroke('Z', InputEvent.CTRL_DOWN_MASK))
      def apply() {
        undoHandler.undoDone()
      }
    })
    contents += new MenuItem(new Action("Redo") {
      accelerator =
        Some(KeyStroke.getKeyStroke('Y', InputEvent.CTRL_DOWN_MASK))
      def apply() {
        undoHandler.redoDone()
      }
    })
    contents += new Menu("Text Manipulation") {
      contents += new MenuItem(Action("Sort Lines") {
        val c = cluster.editor.text.split("\n")
        litheUtils.contentUtils.sortContent(c, _ < _)
      })
      contents += new MenuItem(Action("Sort Words") {
        val c = cluster.editor.text
          .split("\n")
          .flatMap(_.split(" "))
          .filter(_.trim != "")
        litheUtils.contentUtils.sortContent(c, _ < _)
      })

    }
  }

  def viewMenu(): Menu = new Menu("View") {
    contents += new Menu("Text Editor Mode") {
      contents += new MenuItem(new Action("On") {
        accelerator =
          Some(KeyStroke.getKeyStroke(';', InputEvent.CTRL_DOWN_MASK))
        def apply() { goToEditorMode(cluster) }
      })
      contents += new MenuItem(new Action("Off") {
        accelerator =
          Some(KeyStroke.getKeyStroke('L', InputEvent.CTRL_DOWN_MASK))
        def apply() { goToNotepadMode(cluster) }
      })
      contents += new Menu("Choose Language") {
        cluster.const.AVAILABLE_SYNTAX.keys.foreach { lang =>
          contents += new MenuItem(Action(lang) {
            changeSyntax(lang, cluster)
          })
        }
      }
    }
  }

  def toolsMenu(): Menu = new Menu("Tools") {
    contents += new Menu("Spitter") {
      contents += new MenuItem(Action("Run Spitter") {
        SpitterWindow.spitterWindow(cluster)
      })
    }
  }

  def preferencesMenu(fontSizeSlider: Slider): Menu = new Menu("Preferences") {
    contents += new Menu("Themes") {
      contents += new MenuItem(Action("Amy") {
        setEditorTheme(cluster, "Amy")
      })
      contents += new MenuItem(Action("Cobalt") {
        setEditorTheme(cluster, "Cobalt")
      })
      contents += new MenuItem(Action("Eiffel") {
        setEditorTheme(cluster, "Eiffel")
      })
      contents += new MenuItem(Action("Hack") {
        setEditorTheme(cluster, "Hack")
      })
      contents += new MenuItem(Action("Monokai") {
        setEditorTheme(cluster, "Monokai")
      })
      contents += new MenuItem(Action("Cinnamon") {
        setEditorTheme(cluster, "Cinnamon")
      })
      contents += new MenuItem(Action("Lithe") {
        setEditorTheme(cluster, "Lithe")
      })
    }

    contents += new MenuItem(Action("Font Size") {
      fontSizeSliderFrame(fontSizeSlider)
      // Update the slider value, every time the user goes to see it.
      fontSizeSlider.value = cluster.vars.font_size
    })

    contents += new MenuItem(Action("View Default Settings") {
      cluster.editor.editable = true
      cluster.editor.text = litheUtils.fileUtils.getSettingsFile
      cluster.pathKeeper.path = litheUtils.fileUtils.getSettingsPath
    })
  }

  def helpMenu(): Menu = new Menu("Info") {
    contents += new MenuItem(Action("Changelog") {
      if (cluster.popUp.wantToCloseFile()) {
        cluster.info.text = "You are now looking at the changelog"
        cluster.editor.text = litheUtils.fileUtils.getChangeLog
        cluster.editor.editable = false
        cluster.pathKeeper.saved = true
        cluster.top.title = cluster.const.NAME
      }
    })
    contents += new MenuItem(Action("Help") {
      if (cluster.popUp.wantToCloseFile()) {
        cluster.info.text = "Is this what you were looking for?"
        cluster.editor.text = litheUtils.fileUtils.getHelp
        cluster.editor.editable = false
        cluster.pathKeeper.saved = true
        cluster.top.title = cluster.const.NAME
      }
    })
  }

}
