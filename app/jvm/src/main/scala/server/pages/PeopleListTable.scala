package server.pages

import scalatags.Text.TypedTag
import scalatags.Text.all._
import server.pages.gui.PeopleTableHelper
import server.persistency.AppDb
import shared.data.Person
import shared.pages.elements.PeopleListElements.{Classes, Ids}
import shared.resources.css.PeopleListStyles

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.util.Success

/**
  * Shows all the people in the database on the [PeopleListPage].
  * <p>
  * Created by Matthias Braun on 1/21/2017.
  */
object PeopleListTable {

  private def toRow(person: Person): TypedTag[String] =
    tr(td(cls := Classes.name.value)(person.name), td(cls := Classes.age.value)(person.age), td(cls := Classes.occupation.value)(person.occupation))

  private def getPeopleAsRows: Future[TypedTag[String]] =
    AppDb.getAllPeople.map {
      case Success(people) => div(people.map(toRow))
      case error => p(s"Unable to get people from database. Error: $error")
    }

  def create: Future[TypedTag[String]] =
    getPeopleAsRows.map(peopleAsRows =>
      table(id := Ids.table.value, cls := PeopleListStyles.peopleTableClasses)(PeopleTableHelper.head, tbody(peopleAsRows))
    )
}
