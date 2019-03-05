package server.pages.drawing

import server.pages.styles.CssValues

import scalatags.Text.all._
import scalatags.stylesheet.StyleSheet

/**
  * Defines the look of our drawing page using CSS.
  * <p>
  * Created by Matthias Braun on 5/3/2017.
  */
object DrawingPageStyles extends StyleSheet {

  initStyleSheet()

  val canvas = cls(
    border := "1px solid darkslategray"
  )

  val body = cls(
    marginLeft := CssValues.em(4),
    marginRight := CssValues.em(4),
  )
}
