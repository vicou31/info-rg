package server.pages.exampleoverview

import server.pages.styles.CssValues
import server.resources.AppImages

import scalatags.Text.all._
import scalatags.stylesheet.CascadingStyleSheet

/**
  * Contains the styles of the page for navigating to other pages of this site.
  * <p>
  * Created by Matthias Braun on 8/29/17.
  */
object ExamplesOverviewPageStyles extends CascadingStyleSheet {

  initStyleSheet()

  val bodyStyle = cls(
    backgroundImage := CssValues.url(AppImages.examplesOverviewPageBackground),

    paddingLeft := CssValues.em(5),
    paddingRight := CssValues.em(5),
    fontSize := "130%",
  )

  val exampleSection = cls(
    backgroundColor := CssValues.rgba(160, 220, 250, 0.05),
    // Since the section contains a floating image, we need to set overflow so the section expands around the image
    overflow := "auto",
    marginTop := CssValues.em(1),
    padding := CssValues.em(0.5),
    color := CssValues.rgb(25, 0, 25),

    img(
      float := "right",
      marginLeft := CssValues.em(1),
      border := "2px solid lightgrey",
      maxWidth := "40%"
    ),

    // The headings are also links in an example section
    h3(
      a(
        color := CssValues.rgb(50, 40, 50)
      ))
  )

}
