package shared.resources.css

import scalatags.Text.all._
import shared.resources.css.CssValues.url
import shared.resources.path.Images

/**
  * Styles the page giving an overview of all people saved in the database.
  * <p>
  * Created by Matthias Braun on 1/21/2017.
  */
object PeopleListStyles extends SharedStyles with PeopleTableStyles {

  initStyleSheet()

  // Style the page's body
  val bodyStyle = cls(
    backgroundImage := url(Images.pentagonBackground),
    defaultPagePadding,
  )
}
