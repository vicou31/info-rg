package shared.pages.elements

import shared.pages.elements.classes.HtmlClass

/**
  * IDs and classes for the elements on the page showing people saved in the database.
  * <p>
  * Created by Matthias Braun on 5/30/2017.
  */
object PeopleListElements {

  object Ids {
    val table = HtmlId("people-table-id")
  }

  object Classes {
    val name = HtmlClass("people-table-name")
    val age = HtmlClass("people-table-age")
    val occupation = HtmlClass("people-table-occupation")
  }

}
