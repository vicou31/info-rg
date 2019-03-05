package client.ajax

import autowire._
import org.scalajs.dom
import shared.api.{AjaxApi, PeopleAjaxApi}
import shared.data.Person
import shared.serverresponses.ServerResponse

import scala.concurrent.Future
import scala.scalajs.concurrent.JSExecutionContext.Implicits.queue

/**
  * Using Autowire, the client can perform Ajax calls to the server in a type-safe manner.
  * <p>
  * Created by Matthias Braun on 1/9/2017.
  */
object ClientAjaxer extends autowire.Client[String, upickle.default.Reader, upickle.default.Writer] {

  def postPersons(persons: Seq[Person]): Future[ServerResponse] =
    ClientAjaxer[PeopleAjaxApi].postPersons(persons).call()

  override def doCall(req: Request): Future[String] =
    dom.ext.Ajax.post(
      url = "/" + AjaxApi.ajaxRootPath + "/" + req.path.mkString("/"),
      data = upickle.default.write(req.args)
    ).map(xmlHttpRequest => xmlHttpRequest.responseText)

  // There must be a Reader[Result] implicitly available
  def read[Result: upickle.default.Reader](string: String): Result = upickle.default.read[Result](string)

  // There must be a Writer[Result] implicitly available
  def write[Result: upickle.default.Writer](result: Result): String = upickle.default.write(result)
}

