package global

import scala.collection.immutable.ListMap

class GlobalConst(val NAME: String,
                  val MIN_FONT_SIZE: Int,
                  val MAX_FONT_SIZE: Int,
                  val DEFAULT_THEME: String,
                  val DEFAULT_FONT_STYLE: String,
                  val DEFAULT_FONT_SIZE: Int,
                  val DEFAULT_TAB_SIZE: Int,
                  val DEFAULT_MAX_FILE_SIZE: Int,
                  val AVAILABLE_SYNTAX: Map[String, String])

object GlobalConst {

  val syntaxLanguages = ListMap("Bash" -> "bash",
                                "C" -> "c",
                                "C++" -> "cpp",
                                "Clojure" -> "clojure",
                                "DOSBatch" -> "dosbatch",
                                "Groovy" -> "groovy",
                                "Java" -> "java",
                                "Javascript" -> "javascript",
                                "JFlex" -> "jflex",
                                "JSON" -> "json",
                                "Lua" -> "lua",
                                "Properties" -> "properties",
                                "Python" -> "python",
                                "Ruby" -> "ruby",
                                "Scala" -> "scala",
                                "SQL" -> "sql",
                                "TAL" -> "tal",
                                "XHTML" -> "xhtml",
                                "XML" -> "xml",
                                "XPath" -> "xpath")

  def apply(
      NAME: String = "LithePad v0.0.1.1 ",
      MIN_FONT_SIZE: Int = 8,
      MAX_FONT_SIZE: Int = 185,
      DEFAULT_THEME: String = "Monokai",
      DEFAULT_FONT_STYLE: String = "Monospaced",
      DEFAULT_FONT_SIZE: Int = 16,
      DEFAULT_TAB_SIZE: Int = 2,
      DEFAULT_MAX_FILE_SIZE: Int = 10000,
      AVAILABLE_SYNTAX: Map[String, String] = syntaxLanguages): GlobalConst =
    new GlobalConst(NAME,
                    MIN_FONT_SIZE,
                    MAX_FONT_SIZE,
                    DEFAULT_THEME,
                    DEFAULT_FONT_STYLE,
                    DEFAULT_FONT_SIZE,
                    DEFAULT_TAB_SIZE,
                    DEFAULT_MAX_FILE_SIZE,
                    AVAILABLE_SYNTAX)

}
