import sbt.SettingKey

object LogConfig {
  val logDirKey = SettingKey[String]("log-dir", "Defines the location of our log file")
  val logDir = "/logs"
}
