package shared.pages.elements

/**
  * The names and IDs of the HTML elements on the page where the user can upload a file.
  * <p>
  * Created by Matthias Braun on 8/14/17.
  */
object FileUploadElements {

  object Ids {

    val chooseFileInput = HtmlId("choose-file-input-id")
    val fileInfoBox = HtmlId("file-info-box-id")
    val uploadFileButton = HtmlId("upload-file-button-id")

    val fileUploadProgressBar = HtmlId("file-upload-progress-bar-container-id")
    val fileUploadProgressText = HtmlId("file-upload-progress-text-container-id")
    val fileUploadProgressBox = HtmlId("file-upload-progress-box-id")
  }

  object Names {
    val fileInForm = HtmlId("uploaded-file-name-in-form")
  }

}
