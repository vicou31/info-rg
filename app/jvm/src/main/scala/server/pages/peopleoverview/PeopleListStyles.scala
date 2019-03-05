package server.pages.peopleoverview

import server.pages.peopletable.PeopleTableStyles
import server.pages.styles.{CssValues, SharedStyles}
import server.resources.AppImages

import scalatags.Text.all._

/**
  * Styles the page giving an overview of all people saved in the database.
  * <p>
  * Created by Matthias Braun on 1/21/2017.
  */
object PeopleListStyles extends SharedStyles with PeopleTableStyles {

  initStyleSheet()

  // Style the page's body
  val bodyStyle = cls(
    backgroundImage := CssValues.url(AppImages.pentagonBackground),
    defaultPagePadding,
  )
}
