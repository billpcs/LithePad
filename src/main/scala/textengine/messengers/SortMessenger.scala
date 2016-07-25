package textengine.messengers

class SortMessenger {
  val duringMessage = "Sorting lines, please wait"
  val successMessage = "Done sorting"
  val failureMessage = "Failed to sort"
}

object SortMessenger {
  def apply(): SortMessenger = new SortMessenger()
}