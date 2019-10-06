package server.services

import akka.http.scaladsl.server.{Directives, Route}
import server.pages.DrawingPage
import server.pages.utils.http.HttpHelper
import shared.api.AppPaths

/**
  * Provides the services of letting the user draw on a canvas.
  * <p>
  * Created by Matthias Braun on 5/4/2017.
  */
trait DrawingService extends Directives {

  def drawingRoute: Route = (path(AppPaths.drawPage) & get) {
    HttpHelper.logHeadersAndRespondWithEncoded(DrawingPage)
  }
}
