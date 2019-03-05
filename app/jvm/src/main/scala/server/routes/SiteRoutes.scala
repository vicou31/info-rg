package server.routes

import akka.http.scaladsl.model.{ContentTypes, HttpEntity, MediaTypes, StatusCodes}
import akka.http.scaladsl.server.{Directives, Route}
import server.pages.utils.http.HttpHelper
import server.resources.AppImages

/**
  * Routes that pertain to the general web site and not to a specific page or service.
  * <p>
  * Created by Matthias Braun on 7/19/17.
  */
object SiteRoutes extends Directives {

  val faviconRoute: Route =
    path("favicon.ico")(
      getFromResource(AppImages.favicon, MediaTypes.`image/x-icon`)
    )

  val robotsTxtRoute: Route = {
    val robotsTxt = "robots.txt"
    val emptyBody = HttpEntity.empty(ContentTypes.NoContentType)
    path(robotsTxt)(
      // Web crawlers are not allowed to index any pages of this site
      HttpHelper.logHeadersAndRespondWith(emptyBody, StatusCodes.Forbidden, robotsTxt)
    )
  }
}
