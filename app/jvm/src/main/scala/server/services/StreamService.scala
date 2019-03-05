package server.services

import akka.NotUsed
import akka.http.scaladsl.marshalling.Marshalling.Opaque
import akka.http.scaladsl.marshalling.{Marshaller, ToResponseMarshallable}
import akka.http.scaladsl.model.HttpEntity.ChunkStreamPart
import akka.http.scaladsl.model.{ContentTypes, HttpEntity, HttpResponse}
import akka.http.scaladsl.server.{Directives, Route}
import akka.stream.scaladsl.Source
import shared.api.AppPaths
import slogging.LazyLogging

import scala.concurrent.{ExecutionContext, Future}

/**
  * A service that streams data in a chunked response to the client.
  * <p>
  * Created by Matthias Braun on 8/29/17.
  */
trait StreamService extends Directives with LazyLogging {

  /**
    * Asynchronously converts a source of integers into an HTTP response.
    * <p>
    * For synchronous marshallers, see [[Marshaller]].
    */
  private def intSourceToResponse(executionContext: ExecutionContext, intSource: Source[Int, NotUsed]) =
  // Marshalling is executed on a thread provided by the actor system's dispatcher
    Future {
      // Convert the source of integers into a source of ChunkStreamParts
      val chunks = intSource.map(int => ChunkStreamPart(s"Chunk: $int\n"))
      List(
        // Opaque means that the marshalling doesn't involve content negotiation
        Opaque(() =>
          HttpResponse(entity = HttpEntity.Chunked(ContentTypes.`text/plain(UTF-8)`, chunks))
        )
      )
    }(executionContext)

  // We stream this source to the client
  val integerSource = Source(1 to 1000)

  def streamRoute: Route =
    (path(AppPaths.chunkStream) & get) {
      complete {

        // Curry the marshalling function that the marshaller needs
        val marshaller = Marshaller((intSourceToResponse _).curried)
        ToResponseMarshallable(integerSource)(marshaller)
      }
    }
}
