package server.pages.drawing

import server.pages.Page
import server.pages.gui.PageTitles
import shared.pages.elements.{DrawingPageElementIds => Ids}
import shared.pages.{PageId, PageIds}

import scala.concurrent.Future
import scalatags.Text.all._

/**
  * A page that lets the user draw on it.
  * <p>
  * Created by Matthias Braun on 5/3/2017.
  */
object DrawingPage extends Page {
  override def pageId: PageId = PageIds.drawing

  private val pageHeader = defaultHead(pageId, PageTitles.drawing, DrawingPageStyles)

  // Show this when the browser doesn't support the HTML canvas element
  private val canvasNotSupportedText =
    "Your browser doesn't seem to support the canvas element so you're not able to draw here, sorry."

  // Add the CSS styles to the body and the body's contents
  private val pageBody = body(cls := DrawingPageStyles.body.name)(
    h3("Let's draw something 🖌️🎨"),
    // Width and height must be canvas attributes, not style attributes
    canvas(id := Ids.canvasId.value, cls := DrawingPageStyles.canvas.name, widthA := 600, heightA := 400)(canvasNotSupportedText)
  )

  override def mkHtml: Future[String] = Future.successful(htmlWithDocType(pageHeader, pageBody))
}
