package server.pages.peopleform

import server.pages.peopletable.PeopleTableStyles
import server.pages.styles.{CssValues, SharedStyles}
import server.resources.AppImages

import scalatags.Text.all._

/**
  * Contains the classes that define the look of our person form.
  * <p>
  * Created by Matthias Braun on 1/5/2017.
  */
object PeopleFormStyles extends SharedStyles with PeopleTableStyles {

  // If we don't call this, we only the the styles within SharedStyles and none of the styles defined here
  initStyleSheet()

  val bodyStyle = cls(
    backgroundImage := s"url(${AppImages.pentagonBackground})",
    defaultPagePadding,
  )

  val peopleListLink = cls(
    display.`inline-block`,
    marginTop := "30px"
  )

  val infoBox =
    cls(
      backgroundColor := "lightcyan",
      color := "darkslategray",
      display.`inline-block`,
      padding := "10px 10px 10px 10px",
      marginTop := "5px"
    )

  val addRowButton = cls(
    marginTop := CssValues.em(0.5)
  )

  val addRowButtonClasses = s"${addRowButton.name} ${pureButton.v}"
}

