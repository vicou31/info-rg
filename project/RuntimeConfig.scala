
object RuntimeConfig {

  def debugOptions(inDevMode: Boolean) = if (inDevMode)
    Seq(
      "-jvm-debug 5005"
    )
  else Seq()

  def javaRuntimeOptions = Seq(
    // Set maximum Java heap size
    "-J-Xmx1G",
    "-XX:+HeapDumpOnOutOfMemoryError",
    "-XX:+CrashOnOutOfMemoryError",
    s"-XX:ErrorFile=${LogConfig.logDir}/fatal.log"
  )
}
