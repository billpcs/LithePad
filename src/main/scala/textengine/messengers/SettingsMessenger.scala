package textengine.messengers

class SettingsMessenger {
  val couldNotFindSettingsFile = "Could not find settings file"
  val notSupportedSettings = "One or more of the settings are not supported."
  val settingsLoadedSuccessfully = "All of the settings have been added successfully."
}


object SettingsMessenger {
  def apply(): SettingsMessenger = new SettingsMessenger()
}
