

import buildinfo.BuildInfo
import server.AppServer
import slogging._

/**
  * Starts this application.
  */
object Main extends LazyLogging {

  /**
    * Prints runtime information such as the amount of memory and processors available to the JVM.
    */
  private def printRuntimeInfo() = {
    val runtime = Runtime.getRuntime
    val mb = 1024 * 1024
    val maxMemoryInMb = runtime.maxMemory() / mb
    logger.info("JVM max heap memory: {} MB. Available processors: {}", maxMemoryInMb, runtime.availableProcessors)
  }

  def main(args: Array[String]): Unit = {

    // Without this, we wouldn't have log messages
    LoggerConfig.factory = SLF4JLoggerFactory()

    logger.info("Let's start the {} server :-)", BuildInfo.name)
    printRuntimeInfo()

    AppServer.up()
  }
}
