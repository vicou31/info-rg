package server.pages

import scalatags.Text.all._
import server.pages.gui.PageTitles
import shared.pages.{PageId, PageIds}

import scala.concurrent.Future

/**
  * A simple hello world HTML page which the server sends to the client.
  * <p>
  * Created by Matthias Braun on 10/10/2016.
  */
object WelcomePage extends Page {

  override def pageId: PageId = PageIds.hello
  /**
    * Builds this page as a string of HTML.
    *
    * @return the page as an HTML string
    */
  override def mkHtml: Future[String] =
    Future.successful(htmlWithDocType(mkHead, mkBody))


  private def mkBody = body(
    div(
      h1("A minimal Scala.js example"),
      p("The ", b("server"), " added this and was modify by me.  Time to relax ☕")
    )
  )

  private def mkHead = defaultHead(pageId, PageTitles.welcome)
}
