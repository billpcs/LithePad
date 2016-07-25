package textengine.trackers

class KeyPressTracker extends Tracker[Int] {
  private val NEUTRAL_KEY_CODES = Set(16, 17, 18, 20)

  override def toString = {
    set.mkString(" ")
  }

  def onControlMask(): Boolean = {
    set contains 17
  }

  def neutralKeyPressed(c: Int): Boolean = {
    NEUTRAL_KEY_CODES contains c
  }
}
