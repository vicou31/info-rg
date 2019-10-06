package server.pages

import scalatags.Text.all._
import scala.concurrent.ExecutionContext.Implicits.global

import server.pages.gui.PageTitles
import shared.pages.{PageId, PageIds}
import shared.resources.css.PeopleListStyles

import scala.concurrent.Future

/**
  * Shows all the people that we saved in our database.
  * <p>
  * Created by Matthias Braun on 1/21/2017.
  */
object PeopleListPage extends Page {
  override def pageId: PageId = PageIds.peopleList

  override def mkHtml: Future[String] = mkBody.map(pageBody => htmlWithDocType(mkHead, pageBody))

  private def mkHead = defaultHead(pageId, PageTitles.peopleList, PeopleListStyles)

  private def mkBody = PeopleListTable.create.map(table =>
    body(PeopleListStyles.bodyStyle)(
      div(PeopleListStyles.centered)(h1(headline), table)
    )
  )

  val headline = "Saved people"
}
