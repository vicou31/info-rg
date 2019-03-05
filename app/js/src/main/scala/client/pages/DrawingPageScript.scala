package client.pages

import client.utils.{Elements, ErrorMsg}
import org.scalajs.dom._
import org.scalajs.dom.html.Canvas
import shared.pages.elements.{DrawingPageElementIds => Ids}
import slogging.LazyLogging

/**
  * The client code for the page letting the user draw.
  * <p>
  * Created by Matthias Braun on 5/3/2017.
  */
object DrawingPageScript extends ClientScriptHelper with LazyLogging {

  /* We execute our JavaScript after the page was loaded since we access HTML elements of the page which are
  unavailable at the time this script is executed. */
  def execute(): Unit = window.onload = _ => onPageLoaded()

  /** Returns either this [[Canvas]]' [[CanvasRenderingContext2D]] or an [[ErrorMsg]] if that fails. */
  private def getContext2D(canvas: Canvas): Either[ErrorMsg, CanvasRenderingContext2D] =
    if (canvas != null)
      canvas.getContext("2d") match {
        case context: CanvasRenderingContext2D => Right(context)
        case other => Left(ErrorMsg(s"getContext(2d) returned $other"))
      }
    else
      Left(ErrorMsg("Can't get rendering context of null canvas"))

  private def drawOnCanvasWhenMouseMoved(canvas: Canvas): Unit = {
    getContext2D(canvas).fold(
      errorMsg => logger.warn("Couldn't get rendering context of canvas: {}", canvas, errorMsg),
      context => canvas.onmousemove = { e: MouseEvent => drawOnCanvas(e, context) }
    )

    def drawOnCanvas(e: MouseEvent, context: CanvasRenderingContext2D): Unit = {
      val x = e.clientX - canvas.offsetLeft
      val y = e.clientY - canvas.offsetTop

      context.fillStyle = "green"
      context.fillRect(x, y, 4, 4)
    }
  }

  private def onPageLoaded(): Unit =
    Elements.get[Canvas](Ids.canvasId).fold(
      errorMsg => logger.warn("Could not find canvas. Error is {}", errorMsg),
      canvas => drawOnCanvasWhenMouseMoved(canvas)
    )
}

