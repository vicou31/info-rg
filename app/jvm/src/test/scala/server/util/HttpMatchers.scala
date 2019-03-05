package server.util

import org.scalatest.matchers._

import scalaj.http.HttpResponse

/**
  * [[Matcher]]s for testing [[HttpResponse]]s.
  * <p>
  * Created by Matthias Braun on 5/29/2017.
  */
object HttpMatchers {

  def beRedirect = Matcher { response: HttpResponse[_] =>
    MatchResult(
      response.isRedirect,
      s"Response $response is not a redirect, it's a ${response.code}",
      s"Response $response is a redirect"
    )
  }

  def beSuccess = Matcher { response: HttpResponse[_] =>
    MatchResult(
      response.isSuccess,
      s"Response $response is not a success, it's a ${response.code}",
      s"Response $response is a success"
    )
  }

  def beError = Matcher { response: HttpResponse[_] =>
    MatchResult(
      response.isError,
      s"Response $response is not an error, it's a ${response.code}",
      s"Response $response is an error"
    )
  }
}

