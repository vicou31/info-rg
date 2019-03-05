package server.pages.peopleform

import server.pages.Page
import server.pages.gui.{PageTitles, PeopleTableHelper}
import shared.api.AppPaths
import shared.pages.elements.classes.SharedClasses
import shared.pages.elements.{PeopleFormElementIds => Ids}
import shared.pages.{PageId, PageIds}

import scala.concurrent.Future
import scalatags.Text.all._

/**
  * A simple form where users can fill in data about people and submit the data to the server.
  * <p>
  * Created by Matthias Braun on 1/5/2017.
  */
object PeopleFormPage extends Page {

  override def pageId: PageId = PageIds.form

  private val peopleTable =
  // The server only adds the table header. The client provides the table body containing the rows
    table(id := Ids.peopleTable.value, cls := PeopleFormStyles.peopleTableClasses)(PeopleTableHelper.head)

  private val addRowButton =
    button(id := Ids.addRowButton.value, cls := PeopleFormStyles.addRowButtonClasses, `type` := "button")("Add row ➕")

  private val submitButton = button(id := Ids.submitButton.value, cls := PeopleFormStyles.submitButtonClasses, `type` := "button")("Save people")

  override def mkHtml: Future[String] = Future.successful(htmlWithDocType(mkHead, mkBody))

  // Add the CSS styles to the head
  private def mkHead = defaultHead(pageId, PageTitles.form, PeopleFormStyles)

  private val linkToPeopleList = a(PeopleFormStyles.peopleListLink)(href := AppPaths.peopleListPage)("The people you saved")

  /**
    * @return a box containing information for the user from the server. Initially empty and invisible
    */
  private val infoBoxForServerResponses = {
    // The info box is initially hidden
    val hiddenInfoBoxClasses = s"${PeopleFormStyles.infoBox.name} ${SharedClasses.hidden}"
    div(id := Ids.serverResponseInfoBox.value, cls := hiddenInfoBoxClasses)
  }

  private def mkBody =
  // Apply the styles to the body's contents
    body(PeopleFormStyles.bodyStyle)(
      div(PeopleFormStyles.centered)(
        h1("Enter people data"),
        // Styling the table as a form applies the Pure styles to the table's input fields
        form(PeopleFormStyles.pureForm)(peopleTable),
        // Stack the buttons on top of each other using divs
        div(addRowButton),
        div(submitButton),
        div(linkToPeopleList),
        // Informs the user how the server responded to requests such as sending it the person data from the table
        infoBoxForServerResponses
      )
    )
}
