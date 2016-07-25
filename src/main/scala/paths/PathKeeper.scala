package paths

import global.GlobalVars

class PathKeeper(var path: String, globalVars: GlobalVars) {

  private var _saved = false
  private var _newFile = true

  override def toString = {
    if ( globalVars.isSaved )
      s"[ ${this.path} ]"
    else
      s"[ ${this.path} *  ]"
  }

  def saved = _saved

  def saved_=(x: Boolean): Unit = {
    globalVars.isSaved = x
    _saved = x
  }

  // We have to keep track of files that
  // a brand new so that we do not show
  // 'not saved' messages for them, even
  // if the are indeed not saved
  def isnew = _newFile

  def isnew_=(x: Boolean) {
    _newFile = x
  }

}

object PathKeeper {
  def apply(path: String, globalVars: GlobalVars): PathKeeper = {
    new PathKeeper(path, globalVars)
  }
}
