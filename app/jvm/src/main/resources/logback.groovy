import buildinfo.BuildInfo$

import java.nio.file.Paths

// Info on patterns: http://logback.qos.ch/manual/layouts.html#conversionWord
//def location = "%class.%method\\(%file:%line\\)"
logMsgPattern = "%level: '%msg' %d{yyyy-MMM-dd HH:mm:ss}%n"

consoleAppenderName = 'console logger'
fileAppenderName = 'file logger'

appender(consoleAppenderName, ConsoleAppender) {
  encoder(PatternLayoutEncoder) {
    pattern = logMsgPattern
  }
}

appender(fileAppenderName, FileAppender) {
  file = getLogFile()
  encoder(PatternLayoutEncoder) {
    pattern = logMsgPattern
  }
}

root(INFO, [consoleAppenderName, fileAppenderName])

def getLogFile() {
  // We use the log directory that is defined in build.sbt and put into BuildInfo by the sbt-buildinfo plugin
  String logDir = BuildInfo$.MODULE$.logDir()
  String defaultLogFileName = "app.log"
  String logFile = Paths.get(logDir, defaultLogFileName).toString()
  println "Logging to '$logFile'"
  logFile
}

