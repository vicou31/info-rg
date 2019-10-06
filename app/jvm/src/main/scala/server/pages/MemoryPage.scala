package server.pages

import scalatags.Text.all._
import server.pages.gui.PageTitles
import shared.pages.elements.MemoryPageElements.Ids
import shared.resources.css.MemoryStyles
import shared.pages.{PageId, PageIds}

import scala.concurrent.Future

object MemoryPage extends Page{
  override def pageId: PageId = PageIds.memory

  /**
    * Builds this page as a string of HTML.
    *
    * @return the page as an HTML string
    */
  override def mkHtml: Future[String] =
    Future.successful(htmlWithDocType(mkHead, mkBody))

  private def mkBody = body(
    h1(textAlign:="center", fontSize := "20px")("Memory Game"),
    p(fontSize:="15px")(
      b(
        """
          |Choose two cards. If they are the same then the cards will hide else they will flip back.
          |Continue until all pairs of card have been revealed.
        """.stripMargin
      )
    ),
    div(id := "app")
  )

  private def mkHead = defaultHead(pageId, PageTitles.memory)

}
