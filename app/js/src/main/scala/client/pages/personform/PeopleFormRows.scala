package client.pages.personform

import org.scalajs.dom.html.TableRow
import shared.pages.elements.{HtmlId, PeopleFormElementIds => Ids}

import scalatags.JsDom.all._

/**
  * Creates the rows representing a single person in the [[PeopleFormScript]].
  * <p>
  * Created by Matthias Braun on 1/6/2017.
  */
object PeopleFormRows {

  private def textInput(inputId: HtmlId, placeholderText: String) =
    input(id := inputId.value, placeholder := placeholderText)

  private def nameInput(personNr: Int) = textInput(Ids.personName(personNr), "Name")

  private def ageInput(personNr: Int) = textInput(Ids.personAge(personNr), "Age")

  private def occupationInput(personNr: Int) = textInput(Ids.occupation(personNr), "Occupation")

  def create(personNr: Int): TableRow =
    tr(td(nameInput(personNr)), td(ageInput(personNr)), td(occupationInput(personNr))).render
}
