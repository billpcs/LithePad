package colors

import textengine.Cluster

object ThemeSettings {

  /*

   */
  def setThemeForEditorMode(cluster: Cluster) = {
    val colorMaker = new GeneralColoring(cluster.editor)
    colorMaker.theme = "Eiffel"
    cluster.editor.caret.color = cluster.editor.foreground
  }

  def setEditorTheme(cluster: Cluster, theme: String) = {
    val colorMaker = new GeneralColoring(cluster.editor)
    colorMaker.theme = theme
    cluster.editor.caret.color = cluster.editor.foreground
    cluster.vars.theme = theme
    cluster.info.text = s"You updated your theme to $theme !"
  }
}
