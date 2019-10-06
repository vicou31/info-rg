package server.pages

import scalatags.Text.all._
import scalatags.Text.tags2.section
import server.pages.gui.PageTitles
import shared.api.AppPaths
import shared.pages.{PageId, PageIds}
import shared.resources.css.ExamplesOverviewPageStyles
import shared.resources.path.Images

import scala.concurrent.Future

/**
  * A page that lets the user visit all the example pages of this site.
  * <p>
  * Created by Matthias Braun on 8/29/17.
  */
object ExamplesOverviewPage extends Page {
  override def pageId: PageId = PageIds.examplesOverview

  override def mkHtml: Future[String] = {
    def mkBody = {

      val styledSection = section(cls := ExamplesOverviewPageStyles.exampleSection.name)

      body(cls := ExamplesOverviewPageStyles.bodyStyle.name)(
        div(
          h2("Example Pages"),

          styledSection(
            h3(a(href := AppPaths.peopleFormPage)("Save form content in a server database")),
            a(href := AppPaths.peopleFormPage)(img(src := Images.peopleFormGif, alt := "People form animation")),
            p("This page contains a form for entering person data. The user can send the form's content to the server using Ajax. " +
              "The server saves the data in a Postgres database and responds to the client.")
          ),
          styledSection(
            h3(a(href := AppPaths.drawPage)("Draw on a canvas")),
            a(href := AppPaths.drawPage)(img(src := Images.drawOnCanvasGif, alt := "Drawing on canvas animation")),
            p("An HTML5 canvas that you can draw on with the mouse.")
          ),
          styledSection(
            h3(a(href := AppPaths.uploadFilePage)("Upload a file")),
            a(href := AppPaths.uploadFilePage)(img(src := Images.uploadFileGif, alt := "File upload animation")),
            p("A page that lets the user choose a local file and upload it to the server. Client code uses the ",
              a(href := "https://developer.mozilla.org/en-US/docs/Web/API/File")("File API"),
              " for uploading and providing information about the upload progress. The server employs a ",
              a(href := "http://doc.akka.io/docs/akka/current/scala/stream/index.html")("Stream"),
              " to save the file without loading it entirely into memory, thus enabling the server to handle files larger " +
                "than the available memory."
            )
          ),
          styledSection(
            h3(a(href := AppPaths.chunkStream)("Stream data from server")),
            a(href := AppPaths.chunkStream)(img(src := Images.chunkStreamGif, alt := "Stream data from server animation")),
            p("A no-frills example where the server ",
              a(href := "http://doc.akka.io/docs/akka/current/scala/stream/index.html")("streams"), " data to the client in ",
              a(href := "https://en.wikipedia.org/wiki/Chunked_transfer_encoding")("chunked responses"), "."
            )
          )
        )
      )
    }

    Future.successful(htmlWithDocType(
      defaultHead(PageIds.examplesOverview, PageTitles.examplesOverview, ExamplesOverviewPageStyles),
      mkBody
    ))
  }
}
