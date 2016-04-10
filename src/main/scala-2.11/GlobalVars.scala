class GlobalVars( var theme: String,
                  var font_style: String,
                  var font_size: Int,
                  var tab_size: Int,
                  var correctValue: Int,
                  var isSaved: Boolean,
                  var last_tab_size_val: String,
                  var max_file_size: Int,
                  var available_syntaxes: Array[String],
                  var show_hidden_files: Boolean)

object GlobalVars {

  val syntax_languages = "bash, c, clojure, cpp, dosbatch, groovy, java, javascript, lua, properties, python, ruby, scala, sql, tal, xhtml, xml, xpath"
  val syntax_lang_arr = syntax_languages.split(",").map(_.trim)

  def apply(theme: String = "Lithe",
            font_style: String = "Monospaced",
            font_size: Int = 16,
            tab_size: Int = 2,
            correctValue: Int = 28,
            isSaved: Boolean = false,
            last_tab_size_val: String = "2",
            max_file_size: Int = 10000,
            available_syntaxes: Array[String] = syntax_lang_arr,
            show_hidden_files: Boolean = true): GlobalVars =

    new GlobalVars( theme,
                    font_style,
                    font_size,
                    tab_size,
                    correctValue,
                    isSaved,
                    last_tab_size_val,
                    max_file_size,
                    available_syntaxes,
                    show_hidden_files
    )
}