package client.utils

import org.scalajs.dom.Element
import org.scalajs.jquery.jQuery
import shared.pages.elements.HtmlId
import shared.pages.elements.classes.HtmlClass
import slogging.LazyLogging

import scala.scalajs.js

/**
  * Manipulates classes of HTML elements.
  * <p>
  * Created by Matthias Braun on 8/24/17.
  */
object ElementClasses extends LazyLogging {

  def removeClass(elementId: HtmlId, classToRemove: HtmlClass): Unit =
    Elements.get[Element](elementId).fold(
      errorMsg => logger.warn("Can't remove class from element with ID {}. Error: {}", elementId, errorMsg),
      element => removeClass(element, classToRemove)
    )

  def removeClass(element: js.Any, classToRemove: HtmlClass): Unit = jQuery(element).removeClass(classToRemove.value)

  def addClass(elementId: HtmlId, classToAdd: HtmlClass): Unit =
    Elements.get[Element](elementId).fold(
      errorMsg => logger.warn("Can't add class to element with ID {}. Error: {}", elementId, errorMsg),
      element => {
        logger.warn("before")
        addClass(element, classToAdd)
      }
    )

  def addClass(element: js.Any, classToAdd: HtmlClass): Unit = {
    logger.warn("before"+classToAdd)
    jQuery(element).addClass(classToAdd.value)
    logger.warn("error")
  }
}
