package client

import client.pages.personform.PeopleFormScript
import client.pages.{DrawingPageScript, FileUploadPageScript, WelcomePageScript}
import org.scalajs.dom._
import shared.pages.{PageId, PageIds}
import slogging.{LazyLogging, LoggerConfig, PrintLoggerFactory}


object Client extends LazyLogging{
  //Log using Scala's println()
  LoggerConfig.factory = PrintLoggerFactory()

  def main(args: Array[String]): Unit = {
    println("deployment achieved")
    executeClientCodeOnPage()
  }

  /**
    * Executes the client code for the page. The client code is our Scala code transpiled to JavaScript.
    */
  private def executeClientCodeOnPage() =
    PageId(document.head.id) match {
      case PageIds.hello => WelcomePageScript.execute()
      case PageIds.form => PeopleFormScript.execute()
      case PageIds.drawing => DrawingPageScript.execute()
      case PageIds.fileUpload => FileUploadPageScript.execute()
      case otherPageId => logger.warn("There's no client code for a page with this head ID '{}'", otherPageId)

    }
}

