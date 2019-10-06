package server.services

import java.nio.file.Paths

import akka.http.scaladsl.server.{Directives, Route}
import akka.stream.scaladsl.FileIO
import server.pages.FileUploadPage
import server.pages.utils.http.HttpHelper
import shared.api.AppPaths
import shared.pages.elements.FileUploadElements
import slogging.LazyLogging

import scala.util.{Failure, Success}

/**
  * Lets users upload files to the server.
  * <p>
  * Created by Matthias Braun on 7/31/17.
  */
trait FileUploadService extends Directives with LazyLogging {

  def fileUploadRoute: Route =
    (path(AppPaths.uploadFilePage) & get) {
      HttpHelper.logHeadersAndRespondWithEncoded(FileUploadPage)
    } ~ path(AppPaths.uploadFileAction) {
      post {
        extractRequestContext { ctx =>
          fileUpload(FileUploadElements.Names.fileInForm.value) {
            case (fileInfo, fileStream) =>

              // Remember, this path is inside the Docker container
              val destinationPath = Paths.get("/tmp", fileInfo.fileName)

              logger.info(s"Writing to $destinationPath")

              val sink = FileIO.toPath(destinationPath)
              val futureResult = fileStream.runWith(sink)(ctx.materializer)

              onSuccess(futureResult) { result =>
                result.status match {
                  case Success(_) =>
                    val successMsg = s"Successfully wrote ${result.count} bytes to $destinationPath"

                    logger.info(successMsg)
                    complete(successMsg)
                  case Failure(e) => throw e
                }
              }
          }
        }
      }
    }
}
