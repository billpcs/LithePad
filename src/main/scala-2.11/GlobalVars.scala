class GlobalVars( var theme: String,
                  var font_style: String,
                  var font_size: Int,
                  var tab_size: Int,
                  var correctValue: Int,
                  var isSaved: Boolean,
                  var last_tab_size_val: String,
                  var max_file_size: Int)

object GlobalVars {

  def apply(theme: String = "Lithe",
            font_style: String = "Monospaced",
            font_size: Int = 16,
            tab_size: Int = 2,
            correctValue: Int = 28,
            isSaved: Boolean = false,
            last_tab_size_val: String = "2",
            max_file_size: Int = 2000000): GlobalVars =

    new GlobalVars( theme,
                    font_style,
                    font_size,
                    tab_size,
                    correctValue,
                    isSaved,
                    last_tab_size_val,
                    max_file_size
    )
}