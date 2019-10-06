package server.services

import akka.http.scaladsl.server.{Directives, Route}
import server.pages.MemoryPage
import server.pages.utils.http.HttpHelper
import shared.api.AppPaths

trait MemoryService extends Directives {
  def memoryRoute: Route = (path(AppPaths.memoryPage) & get) {
    HttpHelper.logHeadersAndRespondWithEncoded(MemoryPage)
  }
}
