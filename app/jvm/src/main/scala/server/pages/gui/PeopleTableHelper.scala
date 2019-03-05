package server.pages.gui

import scalatags.Text.TypedTag
import scalatags.Text.all._

/**
  * Helps building tables that can be used to enter or show data about people.
  * <p>
  * Created by Matthias Braun on 1/21/2017.
  */
object PeopleTableHelper {
  def head: TypedTag[String] = thead(tr(th("Name"), th("Age"), th("Occupation")))
}
