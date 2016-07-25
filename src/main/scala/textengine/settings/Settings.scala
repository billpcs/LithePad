package textengine.settings

import java.awt.Font

import colors.ThemeSettings._
import textengine.Cluster

class Settings(cluster: Cluster) {

  def isBoolean(bool: String): Boolean = {
    Seq("true", "false").contains(bool)
  }

  def isSupportedTheme(n: String): Boolean = {
    val themes =
      Array("Cobalt", "Eiffel", "Monokai", "Hack", "Amy", "Cinnamon", "Lithe")
    themes contains n
  }

  def updateTheme(theme: String): Unit = {
    setEditorTheme(cluster, theme)
  }

  def isSupportedFontSize(n: Int): Boolean = {
    (cluster.const.MIN_FONT_SIZE to cluster.const.MAX_FONT_SIZE) contains n
  }

  def updateFontSize(size: Int): Unit = {
    cluster.editor.font = new Font(cluster.vars.font_style, java.awt.Font.PLAIN , size)
    cluster.vars.font_size = size
    cluster.fontSizeSlider.value = cluster.vars.font_size
  }

  def isSupportedFontStyle(font: String): Boolean = {
    val fontStyles = Array("Courier", "Monospaced")
    fontStyles contains font
  }

  def updateFontStyle(font: String): Unit = {
    cluster.editor.font = new Font(font, java.awt.Font.PLAIN , cluster.vars.font_size)
    cluster.vars.font_style = font
  }

  def isSupportedTabSize(n: Int): Boolean = {
    (1 to 8) contains n
  }

  def updateTabSize(n: Int): Unit = {
    cluster.tabSize.peer.setSelectedItem(n)
    cluster.vars.tab_size = n
  }

  def isSupportedFileSize(n: Int): Boolean = {
    true
  }

  def updateFileSize(n: Int): Unit = {
    // We want KB not Bytes
    cluster.vars.max_file_size = n*1024
  }

  def isSupportedShowHiddenFiles(bool: String): Boolean = {
    isBoolean(bool)
  }

  def updateShowHiddenFiles(bool: String): Unit = {
    cluster.vars.show_hidden_files = if (bool == "true") true else false
  }

  val theme = new Setting("defaultTheme", "Lithe", isSupportedTheme, List(updateTheme(_)))
  val fontSize = new Setting("fontSize", 16, isSupportedFontSize, List(updateFontSize(_)))
  val fontStyle = new Setting("fontStyle", "Monospaced", isSupportedFontStyle, List(updateFontStyle(_)))
  val tabSize = new Setting("tabSize", 2, isSupportedTabSize, List(updateTabSize(_)))
  val maxFileSize = new Setting("fileSize", 500, isSupportedFileSize, List(updateFileSize(_)))
  val hiddenFiles = new Setting("showHiddenFiles", "true", isSupportedShowHiddenFiles, List(updateShowHiddenFiles(_)))

  def all = List(theme, fontSize, fontStyle, tabSize, maxFileSize, hiddenFiles)

}
