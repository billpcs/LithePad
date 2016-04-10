class GlobalConst(
                  val NAME: String ,
                  val MIN_FONT_SIZE: Int,
                  val MAX_FONT_SIZE: Int ,
                  val FONT_SIZE_STEP: Int ,
                  val DEFAULT_THEME: String ,
                  val DEFAULT_FONT_STYLE: String ,
                  val DEFAULT_FONT_SIZE: Int,
                  val DEFAULT_TAB_SIZE: Int,
                  val DEFAULT_MAX_FILE_SIZE: Int,
                  val AVAILABLE_SYNTAX: Array[String]
                  )

object GlobalConst {

  val syntax_languages = "bash, c, clojure, cpp, dosbatch, groovy, java, javascript, lua, properties, python, ruby, scala, sql, tal, xhtml, xml, xpath"
  val syntax_lang_arr = syntax_languages.split(",").map(_.trim)

  def apply(NAME: String = "LithePad v0.0.0.18 ",
            MIN_FONT_SIZE: Int = 8,
            FONT_SIZE_STEP: Int = 60,
            MAX_FONT_SIZE: Int = 40,
            DEFAULT_THEME: String = "Monokai",
            DEFAULT_FONT_STYLE: String = "Monospaced",
            DEFAULT_FONT_SIZE: Int = 16,
            DEFAULT_TAB_SIZE: Int = 2,
            DEFAULT_MAX_FILE_SIZE: Int = 10000,
            AVAILABLE_SYNTAX: Array[String] = syntax_lang_arr ): GlobalConst =

            new GlobalConst(NAME,
                            MIN_FONT_SIZE,
                            FONT_SIZE_STEP,
                            MAX_FONT_SIZE,
                            DEFAULT_THEME,
                            DEFAULT_FONT_STYLE,
                            DEFAULT_FONT_SIZE,
                            DEFAULT_TAB_SIZE,
                            DEFAULT_MAX_FILE_SIZE,
                            AVAILABLE_SYNTAX
            )

}