package shared.pages.elements

/**
  * Provides IDs of the HTML elements on the people form.
  * <p>
  * Created by Matthias Braun on 1/6/2017.
  */
object PeopleFormElementIds {

  val peopleTable = HtmlId("people-table")

  val peopleTableBody = HtmlId("people-table-body")

  val submitButton = HtmlId("submit-people-button")

  val addRowButton = HtmlId("add-row-button")

  val serverResponseInfoBox = HtmlId("server-response-info")

  def occupation(personNr: Int) = HtmlId(s"occupation-of-person-$personNr")

  def personAge(personNr: Int) = HtmlId(s"age-of-person-$personNr")

  def personName(personNr: Int) = HtmlId(s"name-of-person-$personNr")

}
