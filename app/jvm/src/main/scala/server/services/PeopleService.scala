package server.services

import akka.http.scaladsl.server.{Directives, Route}
import server.pages.peopleform.PeopleFormPage
import server.pages.peopleoverview.PeopleListPage
import server.pages.utils.http.HttpHelper
import server.persistency.AppDb
import shared.api.{AppPaths, PeopleAjaxApi}
import shared.data.Person
import shared.serverresponses.{GotPersonsAndSavedInDb, GotPersonsButErrorWhileSaving, ServerResponse, ServerStringsForClient}
import slogging.LazyLogging

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.util.{Failure, Success}

/**
  * Provides the service of saving people at the server and offering them to the client.
  * <p>
  * Created by Matthias Braun on 5/4/2017.
  */
trait PeopleService extends PeopleAjaxApi with Directives with LazyLogging {

  def peopleRoute: Route =
    (path(AppPaths.peopleFormPage) & get) {
      HttpHelper.logHeadersAndRespondWithEncoded(PeopleFormPage)
    } ~
      (path(AppPaths.peopleListPage) & get) {
        HttpHelper.logHeadersAndRespondWithEncoded(PeopleListPage)
      }

  /** Implements our AjaxApi: The client can post persons and we save them in a database. */
  override def postPersons(persons: Seq[Person]): Future[ServerResponse] = {
    val size = persons.size
    val personOrPersons = if (size == 1) "person" else "persons"
    logger.info("Server got {} {} from client", size, personOrPersons)
    AppDb.save(persons).map {
      case Success(_) => GotPersonsAndSavedInDb(ServerStringsForClient.gotPersons(persons))
      case Failure(error) => GotPersonsButErrorWhileSaving(ServerStringsForClient.errorWhileSavingPersons(persons, error))
    }
  }
}
