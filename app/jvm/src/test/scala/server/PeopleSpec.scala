package server

import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.testkit.ScalatestRouteTest
import org.scalatest.{AsyncWordSpec, Matchers}
import server.pages.PeopleFormPage
import server.services.PeopleService
import shared.api.AppPaths._
import slogging.{LoggerConfig, SLF4JLoggerFactory}

/**
  * Tests the service for saving people on the server and serving them to the client.
  * <p>
  * Created by Matthias Braun on 5/4/2017.
  */
class PeopleSpec extends AsyncWordSpec
  with Matchers
  with ScalatestRouteTest
  with PeopleService {

  LoggerConfig.factory = SLF4JLoggerFactory()

  "The people service" should {
    "return OK if requesting the people form page" in {
      Get(s"/$peopleFormPage") ~> peopleRoute ~> check {
        status shouldEqual StatusCodes.OK
      }
    }
    "return non-empty content when requesting the people form page" in {
      Get(s"/$peopleFormPage") ~> peopleRoute ~> check {
        responseAs[String] should not be empty
      }
    }
    "return the expected content when requesting the people form page" in {
      Get(s"/$peopleFormPage") ~> peopleRoute ~> check {
        val routeResponse = responseAs[String]
        PeopleFormPage.mkHtml.map(pageContent => pageContent shouldEqual routeResponse)
      }
    }

    "leave GET requests to other paths unhandled" in {
      Get("/weDoNotUseThisPath") ~> peopleRoute ~> check {
        handled shouldBe false
      }
    }
  }
}
