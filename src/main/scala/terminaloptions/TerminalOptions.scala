package terminaloptions

import textengine.{Cluster, ContentUtils}

object TerminalOptions {

  def parse(args: Array[String],
            cluster: Cluster,
            contentUtils: ContentUtils): Boolean = {
    val p = new scopt.OptionParser[Unit]("java -jar LithePad.jar") {

      head("Parser for LithePad arguments based on scopt", "3.5.0")

      arg[String]("<file>")
        .foreach(file => {
          cluster.pathKeeper.isnew = false
          contentUtils.loadContentFromPath(file)
          cluster.pathKeeper.path = file
          cluster.pathKeeper.saved = true
        })
        .optional()
        .text("path of the file to open")

      opt[Unit]('v', "version")
        .foreach(x => {
          println(cluster.const.NAME)
          sys.exit()
        })
        .text("current version of LithePad")

      help("help").text("prints this help text")
    }
    p.parse(args)
  }

}
