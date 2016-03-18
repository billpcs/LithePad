class PathKeeper(var path: String, globalVars: GlobalVars) {

  override def toString = {
    if ( globalVars.isSaved )
      s"[ ${this.path} ]"
    else s"[ ${this.path} *  ]"
  }

  def setNotSaved() = {
    globalVars.isSaved = false
  }

  def setSaved() = {
    globalVars.isSaved = true
  }

}

object PathKeeper {
  def apply(path: String, globalVars: GlobalVars): PathKeeper = {
    new PathKeeper(path, globalVars)
  }
}
