package gui

import java.awt.Font

import colors.ThemeSettings._
import de.sciss.syntaxpane.DefaultSyntaxKit
import textengine.Cluster

object ChangeModes {

  def changeFontsToSliderValue(newSize: Int, cluster: Cluster) = {
    cluster.editor.font =
      new Font(cluster.vars.font_style, java.awt.Font.PLAIN, newSize)
    cluster.info.text = s"Font size is set to $newSize"
    newSize
  }

  def goToEditorMode(cluster: Cluster) = {
    val txt = cluster.editor.text
    val syntaxConfig = DefaultSyntaxKit.getConfig(classOf[DefaultSyntaxKit])
    syntaxConfig.put(
        "DefaultFont",
        cluster.vars.font_style + " " + cluster.vars.font_size.toString)
    cluster.editor.contentType = "text/" + cluster.const.AVAILABLE_SYNTAX(
          "Scala")
    cluster.editor.text = txt
    setThemeForEditorMode(cluster)
    cluster.vars.mode = false
    cluster.info.text = "You are now in the text editor mode"
  }

  def goToNotepadMode(cluster: Cluster) = {
    val txt = cluster.editor.text
    cluster.editor.contentType = "text/plain"
    setEditorTheme(cluster, cluster.vars.theme)
    cluster.editor.text = txt
    cluster.vars.mode = true
    cluster.info.text = "You are now in the notepad mode"
  }

  def changeSyntax(lang: String, cluster: Cluster) = {
    goToEditorMode(cluster)
    val availableSyntax = cluster.const.AVAILABLE_SYNTAX
    val txt = cluster.editor.text
    if (availableSyntax.keys.exists(_ == lang)) {
      cluster.editor.contentType = "text/" + availableSyntax(lang)
      cluster.editor.text = txt
      cluster.info.text = s"You updated the syntax to $lang"
    } else {
      cluster.info.text = s"Can not find syntax pack for $lang"
    }
  }

}
