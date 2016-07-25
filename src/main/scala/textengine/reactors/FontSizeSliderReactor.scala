package textengine.reactors

import textengine.Cluster

class FontSizeSliderReactor(cluster: Cluster) {
  def valueChangedReact() = {
    cluster.vars.font_size = gui.ChangeModes
      .changeFontsToSliderValue(cluster.fontSizeSlider.value, cluster)
  }
}
