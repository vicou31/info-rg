package shared.pages

/**
  * Identifies web pages of this application.
  * The client code uses this ID to know which page it got from the server and thus which JavaScript code
  * to execute.
  * <p>
  * Created by Matthias Braun on 10/24/2016.
  */
case class PageId(value: String) extends AnyVal {
  override def toString: String = value
}

/**
  * Contains the IDs of all our web pages.
  */
object PageIds {

  val form = PageId("form-page-id")

  val hello = PageId("hello-page-id")

  val examplesOverview = PageId("examples-overview-page-id")

  val peopleList = PageId("people-list-page-id")

  val drawing = PageId("drawing-page-id")

  val fileUpload = PageId("file-upload-page-id")

  val notFound = PageId("page-not-found-id")
}
