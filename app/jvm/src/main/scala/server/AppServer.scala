package server

import akka.actor.ActorSystem
import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.server.{RejectionHandler, Route}
import akka.http.scaladsl.{ConnectionContext, Http}
import akka.stream.ActorMaterializer
import server.config.ServerConfig
import server.pages.{ExamplesOverviewPage, NotFoundPage, WelcomePage}
import server.pages.utils.http.HttpHelper
import server.routes.SiteRoutes
import server.routes.ajax.ServerAjaxer
import server.services._
import shared.api.AppPaths
import slogging.LazyLogging

import scala.util.{Failure, Success}

/**
  * The server lets clients use a couple of services such as saving people data to a database, uploading files, and
  * streaming data.
  */
object AppServer extends PeopleService
    with FileUploadService
    with StreamService
    with DrawingService
    with MemoryService
    with LazyLogging {

  /**
    * Binds the server to the specified port where it handles TCP connections.
    *
    * @param connectionContext
    *                          the [[ConnectionContext]] to use for handling the connections.
    *                          [[akka.http.scaladsl.HttpsConnectionContext]], for example
    * @param port
    *                          the TCP port where the server will listen for connections
    * @param actorSystem
    *                          used to create and run the server's actors which handle HTTP requests
    * @param actorMaterializer assigns threads to the actors
    */
  private def bindAndHandle(connectionContext: ConnectionContext, port: Int)
                           (implicit actorSystem: ActorSystem, actorMaterializer: ActorMaterializer): Unit = {

    val futureBinding = Http().bindAndHandle(
      handler = Route.handlerFlow(routes),
      interface = ServerConfig.interface,
      port = port,
      connectionContext = connectionContext
    )

    // Log when the server binding is done
    futureBinding.onComplete {
      case Success(binding) =>
        logger.info(s"Server inside Docker container listens on port ${binding.localAddress.getPort}")
      case Failure(cause) =>
        logger.warn("Could not start server. Cause: {}", cause.getMessage, cause)
    }(actorSystem.dispatcher) // The dispatcher provides threads for executing the future's callbacks
  }

  /**
    * Starts the server.
    */
  def up(): Unit = {
    // Runs the server's actors which handle HTTP requests
    implicit val actorSystem = ActorSystem("info-rg-actor-system")

    // The materializer assigns threads to the actors
    implicit val materializer = ActorMaterializer()

    // Listen for HTTPS connections
    /*ServerConfig.TLS.connectionContext match {
      case Success(connectionContext) => bindAndHandle(connectionContext, ServerConfig.TLS.port)
      case Failure(exception) => logger.warn("Could not create TLS connection context", exception)
    }*/

    // Also listen for unencrypted HTTP connections
    bindAndHandle(ConnectionContext.noEncryption, ServerConfig.port)
  }

  /** Defines the HTTP requests our server accepts and how it responds. */
  private def routes: Route =
    handleRejections(rejectionHandler) {
      // Include the encoded JavaScript files in the response
      encodeResponse(getFromResourceDirectory("")) ~
        // Redirect to the welcome page when there is no resource in the URL
        get(pathSingleSlash(redirect(AppPaths.welcomePage, StatusCodes.TemporaryRedirect))) ~
        (path(AppPaths.welcomePage) & get) (HttpHelper.logHeadersAndRespondWithEncoded(WelcomePage)) ~
        (path(AppPaths.exampleOverviewPage) & get) (HttpHelper.logHeadersAndRespondWithEncoded(ExamplesOverviewPage)) ~
        // The people route leads clients to the pages of the people service where they can enter and retrieve person data
        peopleRoute ~
        fileUploadRoute ~
        streamRoute ~
        drawingRoute ~
        memoryRoute ~
        SiteRoutes.faviconRoute ~
        SiteRoutes.robotsTxtRoute ~
        // Respond to an Ajax request
        ServerAjaxer.respond
    }


  /** Handles the case when the client tries to visit a page of this application that doesn't exist. More generally,
    * we send this respond for every HTTP request that can't be handled by the server's routes. */
  private def rejectionHandler =
    RejectionHandler.newBuilder()
      .handleNotFound(HttpHelper.logHeadersAndRespondWithEncoded(NotFoundPage, StatusCodes.NotFound))
      .result()

}
