package textengine.settings

class Setting[T](val name: String,
                 val default: T,
                 isSupported: T => Boolean,
                 var updateFunctions: List[T => Unit]) {

  private var _value = default

  def value: T = _value

  def value_=(newVal: T): Unit = {
    if (isSupported(newVal)) {
      _value = newVal
      updateFunctions.foreach(f => f(newVal))
    }
  }

  override def toString = {
    s"$name = ${_value}"
  }
}
