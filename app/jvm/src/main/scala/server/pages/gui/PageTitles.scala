package server.pages.gui

/**
  * Provides the titles of web pages.
  * <p>
  * Created by Matthias Braun on 1/4/2017.
  */
object PageTitles {

  val welcome = PageTitle("Hello there")
  val examplesOverview = PageTitle("Examples")
  val form = PageTitle("Person form")
  val peopleList = PageTitle("Saved people")
  val drawing = PageTitle("Drawing")
  val uploadFile = PageTitle("Upload file")
  val memory= PageTitle("Memory App")
  val notFound = PageTitle("Page not found")
}

case class PageTitle(value: String) extends AnyVal {
  override def toString: String = value
}
