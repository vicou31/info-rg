package server.pages.fileupload

import server.pages.Page
import server.pages.gui.PageTitles
import shared.pages.{PageId, PageIds}

import scala.concurrent.Future
import scalatags.Text.all._
import scalatags.Text.tags2.progress
import shared.pages.elements.FileUploadElements.{Ids => ElementIds}
import shared.pages.elements.classes.SharedClasses

/**
  * This page lets the user upload a file to the server.
  * <p>
  * Created by Matthias Braun on 8/14/17.
  */
object FileUploadPage extends Page {
  override def pageId: PageId = PageIds.fileUpload

  /**
    * Builds this page as a string of HTML.
    *
    * @return the page as HTML
    */
  override def mkHtml: Future[String] = Future.successful(htmlWithDocType(mkHead, mkBody))

  private val uploadFileButton = {
    val disabledSubmitButton = s"${FileUploadPageStyles.submitButtonClasses} ${SharedClasses.disabled}"

    // We disable this button until the user has selected a file
    button(cls := disabledSubmitButton)(id := ElementIds.uploadFileButton.value)("Upload file")
  }

  private val chooseFileButton = {
    val inputId = ElementIds.chooseFileInput.value
    div(
      div(label(cls := FileUploadPageStyles.fileLabelClasses)(`for` := inputId)("Choose a file")),
      input(FileUploadPageStyles.fileInputStyle)(`type` := "file", id := inputId),
    )
  }

  /**
    * @return a box containing information about the file to upload. Initially empty and invisible
    */
  private val fileInfoBox = {
    val hiddenInfoBoxClasses = s"${FileUploadPageStyles.fileInfoBox.name} ${SharedClasses.hidden}"

    div(id := ElementIds.fileInfoBox.value, cls := hiddenInfoBoxClasses)
  }

  private val fileUploadProgressBox = {
    val progressBar = progress(id := ElementIds.fileUploadProgressBar.value)
    val progressText = div(id := ElementIds.fileUploadProgressText.value)

    val hiddenInfoBoxClasses = s"${FileUploadPageStyles.progressInfoBox.name} ${SharedClasses.hidden}"
    div(id := ElementIds.fileUploadProgressBox.value, cls := hiddenInfoBoxClasses)(progressBar, progressText)
  }

  private def mkBody = body(FileUploadPageStyles.bodyStyle)(
    div(FileUploadPageStyles.centered)(
      h1("Let's upload a file to the server"),
      chooseFileButton,
      // Shows information about the file to upload
      div(fileInfoBox),
      // A container that informs the user about the progress of the upload using a bar and text
      div(fileUploadProgressBox),
      div(uploadFileButton)
    )
  )


  private def mkHead = defaultHead(pageId, PageTitles.uploadFile, FileUploadPageStyles)
}
