package textengine.reactors

import textengine.{Cluster, TextUtils}

class TabSizeComboBoxReactor(cluster: Cluster, textUtils: TextUtils) {
  def selectionChangedReact() = {
    val new_val = cluster.tabSize.peer.getSelectedItem.toString
    if (cluster.vars.last_tab_size_val != new_val) {
      textUtils.changeTabSize(new_val)
      cluster.vars.last_tab_size_val = new_val
    }
  }
}
