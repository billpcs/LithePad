package textengine.reactors

import textengine.{Cluster, TextUtils}
import textengine.trackers.KeyPressTracker

import scala.swing.event.{KeyPressed, KeyReleased}

class KeyReactor(cluster: Cluster,
                 keyPressTracker: KeyPressTracker,
                 textUtils: TextUtils) {

  def keyPressed(change: KeyPressed): Unit = {
//    if (cluster.editor.editable) {
//      keyPressTracker.push(change.peer.getKeyCode)
//      if ((cluster.vars.isSaved || cluster.pathKeeper.path == "") &&
//        !keyPressTracker.onControlMask() && !keyPressTracker.neutralKeyPressed(change.peer.getKeyCode)) {
//        if (cluster.pathKeeper.isnew) cluster.pathKeeper.isnew = false
//        cluster.pathKeeper.saved = false
//        cluster.top.title = cluster.const.NAME + cluster.pathKeeper
//      }
//    }
  }

  def keyReleased(change: KeyReleased): Unit = {
    if (change.peer.getKeyCode == 17) keyPressTracker.reset()
    keyPressTracker.pop(change.peer.getKeyCode)
    cluster.line.text = textUtils.caretUpdate
  }

}
