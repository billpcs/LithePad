package global

class GlobalVars(var theme: String,
                 var font_style: String,
                 var font_size: Int,
                 var tab_size: Int,
                 var isSaved: Boolean,
                 var last_tab_size_val: String,
                 var max_file_size: Int,
                 var show_hidden_files: Boolean,
                 var mode: Boolean)

object GlobalVars {

  def apply(theme: String = "Lithe",
            font_style: String = "Monospaced",
            font_size: Int = 16,
            tab_size: Int = 2,
            isSaved: Boolean = false,
            last_tab_size_val: String = "2",
            max_file_size: Int = 10000,
            show_hidden_files: Boolean = true,
            mode: Boolean = true): GlobalVars =
    new GlobalVars(theme,
                   font_style,
                   font_size,
                   tab_size,
                   isSaved,
                   last_tab_size_val,
                   max_file_size,
                   show_hidden_files,
                   mode)
}
