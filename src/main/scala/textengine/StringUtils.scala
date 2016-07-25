package textengine

object StringUtils {

  def toTitleCase(s: String) = s(0).toUpper + s.substring(1).toLowerCase

  def getStringAmongTwoStrings(str: String, start: String = "=", end: String = ";"): String =
    str.split(start).last.split(end)(0).trim

  private def removeTabs(str: String): String = {
    str.replaceAll("\t", " ")
  }

  private def removeNewLines(str: String): String = {
    str.replaceAll("\n", " ")
  }

  def clearFromSpecialChars(str: String): String = {
    removeTabs(removeNewLines(str))
  }

}