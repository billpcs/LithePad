package textengine.trackers

class Tracker[T] {
  protected val set = collection.mutable.Set.empty[T]

  def push(elem: T): Unit = {
    set.add(elem)
  }

  def pop(elem: T): Unit = {
    set.remove(elem)
  }

  def reset() = {
    set.clear()
  }
}
