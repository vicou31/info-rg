package server.pages.peopletable

import server.pages.styles.SharedStyles

/**
  * Defines the look of the table used for entering and showing people data.
  * <p>
  * Created by Matthias Braun on 8/24/17.
  */
trait PeopleTableStyles extends SharedStyles {
  initStyleSheet()

  val peopleTableClasses = s"${centeredTable.name} ${pureTable.v}"
}
