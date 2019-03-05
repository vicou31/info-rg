package server.pages

import server.pages.gui.PageTitles
import shared.pages.{PageId, PageIds}

import scala.concurrent.Future
import scalatags.Text.all._

/**
  * We show this page when the client requests a page that doesn't exist (i.e., all the routes rejected the request).
  * <p>
  * Created by Matthias Braun on 1/5/2017.
  */
object NotFoundPage extends Page {
  override def pageId: PageId = PageIds.notFound

  private def mkHead = defaultHead(pageId, PageTitles.notFound)

  private def mkBody = p("I'm sorry, the page you requested doesn't exist")

  override def mkHtml: Future[String] = Future.successful(htmlWithDocType(mkHead, mkBody))
}
