package shared.api

import shared.data.Person
import shared.serverresponses.ServerResponse

import scala.concurrent.Future

/**
  * Defines the Ajax calls the client can make to the server for the person service.
  * <p>
  * Created by Matthias Braun on 1/9/2017.
  */
trait PeopleAjaxApi {

  def postPersons(persons: Seq[Person]): Future[ServerResponse]
}

/**
  * Provides paths for the Ajax API.
  */
object AjaxApi {
  // Used for performing Ajax calls from the client to the server
  val ajaxRootPath = "peopleApiAjax"
}
