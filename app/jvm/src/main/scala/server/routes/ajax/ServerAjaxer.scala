package server.routes.ajax

import akka.http.scaladsl.server.{Directives, Route}
import server.AppServer
import server.pages.utils.http.HeaderLogger
import shared.api.{AjaxApi, PeopleAjaxApi}
import slogging.LazyLogging
import upickle.default.{Reader, Writer}

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

/**
  * Lets the server respond to Ajax requests from the client.
  * <p>
  * Created by Matthias Braun on 1/21/2017.
  */
object ServerAjaxer extends Directives with LazyLogging {

  /** Creates a [[Route]] that responds to an Ajax request. The response is not encoded. */
  def respond: Route =
    post(
      path(AjaxApi.ajaxRootPath / Segments)(methodToCall =>
        decodeRequest(
          entity(as[String])(requestData =>
            HeaderLogger.logAjax(methodToCall)(
              // We don't encode since encoding a small (~100 B) response makes it a few bytes larger
              complete(handleAjaxRequest(methodToCall, requestData))
            )
          )
        )
      )
    )

  /**
    * Used for Ajax calls from the client.
    */
  private object AjaxRouter extends autowire.Server[String, Reader, Writer] {

    // There must be a Reader[Result] implicitly available
    def read[Result: Reader](string: String): Result = upickle.default.read[Result](string)

    // There must be a Writer[Result] implicitly available
    def write[Result: Writer](result: Result): String = upickle.default.write(result)
  }

  /**
    * Handles an Ajax request sent from the client by calling a method on the [[AppServer]].
    *
    * @param methodToCall
    *                    contains the package, the class, and the name of the API method to call on the [[AppServer]]
    * @param requestData the request data from the client, such as JSON
    * @return the future answer to the request
    */
  private def handleAjaxRequest(methodToCall: List[String], requestData: String): Future[String] = {

    // Turn the JSON inside the request into a map
    val requestDataAsMap = upickle.default.read[Map[String, String]](requestData)
    val request = autowire.Core.Request(methodToCall, requestDataAsMap)
    // Make the server respond to the request
    AjaxRouter.route[PeopleAjaxApi](AppServer)(request)
  }
}
