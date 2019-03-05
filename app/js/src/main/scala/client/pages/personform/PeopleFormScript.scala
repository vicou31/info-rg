package client.pages.personform

import java.util.UUID

import client.ajax.ClientAjaxer
import client.pages.ClientScriptHelper
import client.utils.{Elements, ErrorMsg}
import org.scalajs.dom._
import org.scalajs.dom.ext.AjaxException
import org.scalajs.dom.html.{Button, Div, Table, TableSection}
import shared.data.Person
import shared.pages.elements.{HtmlId, PeopleFormElementIds => Ids}
import shared.serverresponses.{GotPersonsAndSavedInDb, GotPersonsButErrorWhileSaving, ServerResponse}
import shared.utils.Eithers
import slogging.LazyLogging

import scala.scalajs.concurrent.JSExecutionContext.Implicits.queue
import scala.scalajs.js.Date
import scalatags.JsDom.all._

/**
  * The client code for the people form. In each row of the form, the user can fill in data about a person, add new
  * rows to the form, and submit the person data to the server.
  * <p>
  * Created by Matthias Braun on 1/6/2017.
  */
object PeopleFormScript extends ClientScriptHelper with LazyLogging {

  private def personFromRow(rowNr: Int): Either[ErrorMsg, Person] = {

    // Create a short alias for the function that retrieves the string inside an input field
    def getVal: HtmlId => Either[ErrorMsg, String] = Elements.getValueOfInput

    for {
      name <- getVal(Ids.personName(rowNr))
      age <- getVal(Ids.personAge(rowNr))
      occupation <- getVal(Ids.occupation(rowNr))
    } yield Person(UUID.randomUUID(), name, age, occupation)
  }

  private def getPersonsFromTable: Either[ErrorMsg, Seq[Person]] =
    Elements.get[Table](Ids.peopleTable).flatMap(table => {
      // We subtract one from the number of rows since we don't parse the table's header row, only the rows of its body
      val tableBodyRows = table.rows.length - 1

      val errorMsgsAndPersons = (1 to tableBodyRows).map(personFromRow)

      // Return either the first error message or all the persons from the table
      val errorOrPersons = Eithers.sequence(errorMsgsAndPersons)

      // Filter out empty persons (i.e, persons from those rows where the user hasn't put any text)
      errorOrPersons.map(_.filter(_.nonEmpty))
    })

  private def addRow(): Any = {
    logger.info("Add row button was clicked")

    Elements.get[TableSection](Ids.peopleTableBody).fold(
      errorMsg => logger.warn("Didn't find table body so we can't add a row to it: {}", errorMsg),
      tableBody => {
        // Our table rows start at #1
        val nextTableRowNr = 1 + tableBody.rows.length
        logger.info("Adding row #{}", nextTableRowNr)
        // Append a row
        tableBody.appendChild(PeopleFormRows.create(nextTableRowNr))
      }
    )
  }

  /**
    * Puts some `infoText` into the info box below the form.
    *
    * @param infoText text to show in the box. Messages from the server, for example
    */
  private def setTextInInfoBox(infoText: String): Unit =
    Elements.get[Div](Ids.serverResponseInfoBox) match {
      case Right(infoBox) =>
        val currentTime = new Date().toLocaleTimeString()
        Elements.setChild(infoBox, span(s"$currentTime: $infoText").render)
        show(infoBox)

      case Left(error) => logger.warn("Couldn't find info box to show server response: {}", error)
    }

  private def toInfoAboutServerResponse(serverResponse: ServerResponse) =
    serverResponse match {
      case GotPersonsAndSavedInDb(msg) => s"Server responded: $msg"
      case GotPersonsButErrorWhileSaving(errorMsg) => s"Server got persons but couldn't save them: $errorMsg"
      case other => s"Unexpected server response: $other"
    }

  private def toInfoAboutFailedCallToServer: PartialFunction[Throwable, String] = {
    case AjaxException(request) =>
      s"Ajax call failed. Status: ${request.status}. Status text: ${request.statusText}."
    case otherError =>
      s"Could not contact server. Exception: $otherError. Message: ${otherError.getMessage}."
  }

  private def submitPersonForm(): Any = {
    logger.info("Submit button was clicked")
    getPersonsFromTable.fold(
      errorMsg => logger.warn("Could not get persons from page: {}", errorMsg),
      persons => {
        ClientAjaxer.postPersons(persons)
          // We got a server response -> Turn it to a string
          .map(toInfoAboutServerResponse)
          // We couldn't send the persons -> Turn the exception into a string
          .recover(toInfoAboutFailedCallToServer)
          // Show the info about success or failure to the user
          .map(setTextInInfoBox)
      }
    )
  }

  /* We execute our JavaScript after the page was loaded since we access HTML elements of the page, which are
  unavailable at the time this script is executed. */
  def execute(): Unit = window.onload = _ => onPageLoaded()

  private def addButtonListeners(): Unit = {
    Elements.get[Button](Ids.submitButton).fold(
      errorMsg => logger.warn("Couldn't find submit button on person form: {}", errorMsg),
      button => button.onclick = _ => submitPersonForm()
    )

    Elements.get[Button](Ids.addRowButton).fold(
      errorMsg => logger.warn("Couldn't find add row button on person form: {}", errorMsg),
      button => button.onclick = _ => addRow()
    )
  }

  private def addRowsToTable(nrOfRowsToCreate: Int): Any =
    Elements.get[Table](Ids.peopleTable).fold(
      errorMsg => logger.warn("Didn't find table so we can't add a row to it. Error: {}", errorMsg),
      table => {
        val tableBody = tbody(id := Ids.peopleTableBody.value)((1 to nrOfRowsToCreate).map(PeopleFormRows.create))
        table.appendChild(tableBody.render)
      }
    )


  private def onPageLoaded(): Unit = {

    // We show this many person rows in the table initially
    val nrOfPersonsInTable = 3
    addRowsToTable(nrOfPersonsInTable)

    addButtonListeners()
  }
}
