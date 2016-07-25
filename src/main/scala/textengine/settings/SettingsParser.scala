package textengine.settings

import java.io.FileNotFoundException
import java.util.Properties

import textengine.Cluster
import textengine.messengers.Messenger

import scala.collection.JavaConversions._

object SettingsParser {

  def parseSettings(cluster: Cluster, messenger: Messenger): Settings = {

    val properties = new Properties
    try {
      properties.load(getClass.getResourceAsStream("/settings.properties"))
    } catch {
      case e: FileNotFoundException =>
        cluster.info.text =
          messenger.settingsMessenger.couldNotFindSettingsFile
    }

    val settings = new Settings(cluster)
    var unsupported_setting = false

    for (key <- properties.stringPropertyNames()) {
      val value = properties.getProperty(key)
      key match {
        case "defaultTheme" =>
          settings.theme.value = value
        case "fontStyle" =>
          settings.fontStyle.value = value
        case "fontSize" =>
          settings.fontSize.value = value.toInt
        case "tabSize" =>
          settings.tabSize.value = value.toInt
        case "maxFileSize" =>
          settings.maxFileSize.value = value.toInt
        case "showHiddenFiles" =>
          settings.hiddenFiles.value = value
        case _ =>
          unsupported_setting = true
      }
    }

    if (unsupported_setting) {
      cluster.info.text = messenger.settingsMessenger.notSupportedSettings
    } else {
      cluster.info.text =
        messenger.settingsMessenger.settingsLoadedSuccessfully
    }
    settings
  }

}
