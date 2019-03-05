package client.pages


import client.utils.Elements
import org.scalajs.dom._
import org.scalajs.dom.html.{Button, Div, Input, Progress}
import shared.api.AppPaths
import shared.pages.elements.{FileUploadElements, HtmlId}
import shared.pages.elements.FileUploadElements.{Ids => ElementIds}
import slogging.LazyLogging

import scala.scalajs.js
import scalatags.JsDom.all._
import scala.scalajs.js.JSON

/**
  * This is the client code for the page that lets the user upload a file to the server.
  * <p>
  * Created by Matthias Braun on 8/14/17.
  */
object FileUploadPageScript extends ClientScriptHelper with LazyLogging {

  /* We execute our JavaScript after the page was loaded since we access HTML elements of the page, which are
  unavailable at the time this script is executed. */
  def execute(): Unit = window.onload = _ => onPageLoaded()

  /**
    * Converts a [[FileList]] to a [[List]] of [[File]]s.
    *
    * @param fileList we convert this [[FileList]]
    * @return a [[List]] of [[File]]s
    */
  private def toList(fileList: FileList) =
    (0 to fileList.length)
      .foldLeft(List[File]()) { (files, index) => files :+ fileList(index) }


  /**
    * Converts a number of bytes into a human readable string such as 2.2 MB or 8.0 EiB.
    *
    * @param bytes the number of bytes we want to convert
    * @param si    if true, we use base 10 SI units where 1000 bytes are 1 kB.
    *              If false, we use base 2 IEC units where 1024 bytes are 1 KiB.
    * @return the bytes as a human-readable string
    */
  def humanReadableSize(bytes: Long, si: Boolean): String = {

    // See https://en.wikipedia.org/wiki/Byte
    val (baseValue, unitStrings) =
      if (si)
        (1000, Vector("B", "kB", "MB", "GB", "TB", "PB", "EB", "ZB", "YB"))
      else
        (1024, Vector("B", "KiB", "MiB", "GiB", "TiB", "PiB", "EiB", "ZiB", "YiB"))

    def getExponent(curBytes: Long, baseValue: Int, curExponent: Int = 0): Int =
      if (curBytes < baseValue) curExponent
      else {
        val newExponent = 1 + curExponent
        getExponent(curBytes / (baseValue * newExponent), baseValue, newExponent)
      }

    val exponent = getExponent(bytes, baseValue)
    val divisor = Math.pow(baseValue, exponent)
    val unitString = unitStrings(exponent)

    // Divide the bytes and show one digit after the decimal point
    f"${bytes / divisor}%.1f $unitString"
  }

  private def onPageLoaded(): Unit = {

    logger.info("The file upload page was loaded 👍")

    Elements.get[Input](ElementIds.chooseFileInput)
      .flatMap(input => {
        input.onchange = _ => onChooseFileInputChanged()

        Elements.get[Button](ElementIds.uploadFileButton)
          .map(button => button.onclick = _ => uploadFile(input))
      })

    def onChooseFileInputChanged(): Unit = {

      logger.info("User chose new file")

      /**
        * Shows the name (and maybe the size) of the file to upload
        *
        * @param file the [[File]] the user wants to upload to the server
        */
      def showFileInfo(file: File): Unit =
        Elements.get[Div](ElementIds.fileInfoBox) match {
          case Right(infoBox) =>
            logger.warn("File size:", file.size + "//" +file.size.toLong)
            println("File size:" + file.size + "//" +file.size.toLong)
            val fileSizeText = humanReadableSize(file.size.toLong, si = true)
            val fileNameAndSize = div(s"${file.name}", div(s"$fileSizeText")).render
            Elements.setChild(infoBox, fileNameAndSize)
            show(infoBox)

          case Left(error) => logger.warn("Couldn't find info box to show file info: {}", error)
        }

      // Hide the progress box from the previous upload
      hide(ElementIds.fileUploadProgressBox)

      // Enable the button for uploading the file
      enable(ElementIds.uploadFileButton)

      Elements.get[Input](ElementIds.chooseFileInput)
        .fold(
          errorMsg => logger.warn("Could not get file input: {}", errorMsg),
          fileInput => {
            logger.warn("filesize : "+ fileInput.size.toLong, "//"+fileInput.size)
            getFirstFile(fileInput)
              .foreach(file => showFileInfo(file))
          }
        )
    }

    def postInFormAsync(fileInForm: HtmlId, uploadFileActionPath: String, fileToUpload: File,
                        callbacks: FileUploadCallbacks): Unit = {

      // Put the file to upload into a form which we'll upload to the server
      val formData = new FormData
      formData.append(fileInForm.value, fileToUpload, fileToUpload.name)

      val request = new XMLHttpRequest

      request.upload.onloadstart = callbacks.onLoadStart
      request.upload.onprogress = callbacks.onProgress
      request.upload.onabort = callbacks.onAbort
      request.upload.onerror = callbacks.onError
      request.upload.onload = callbacks.onLoad
      request.upload.ontimeout = callbacks.onTimeOut
      request.upload.onloadend = callbacks.onLoadEnd

      request.open("POST", uploadFileActionPath, async = true, "noUser", "noPassword")
      request.send(formData)
    }

    def callbacks: FileUploadCallbacks = {
      import ElementIds._

      Elements.get[Div](fileUploadProgressBox)
        .flatMap(progressInfoBox =>
          Elements.get[Progress](fileUploadProgressBar)
            .flatMap(progressBar =>
              Elements.get[Div](fileUploadProgressText)
                .map(progressText => {

                  val showInitialProgress = (_: Any) => {
                    // The bar displays the file upload progress in percent
                    progressBar.value = 0
                    progressBar.max = 100
                    progressText.textContent = "0 %"

                    // Progress box is initially hidden
                    show(progressInfoBox)

                    // Progress bar is hidden when we finished uploading the previous file
                    show(progressBar)
                  }

                  val updateProgressInfo = (event: ProgressEvent) =>
                    if (event.lengthComputable) {

                      // Getting the file sizes of larger (e.g., 3.3 GB) files causes this error:
                      // uncaught exception: scala.scalajs.runtime.UndefinedBehaviorError:
                      // An undefined behavior was detected: 3338042390 is not an instance of java.lang.Integer  (unknown)
                      val fileSize = event.total

                      val progressInPercentRounded = Math.round(event.loaded.toDouble / fileSize.toDouble * 100)

                      progressBar.value = progressInPercentRounded
                      progressText.textContent = s"$progressInPercentRounded %"

                    } else {
                      progressText.textContent = "Size of file to upload is not computable"
                    }

                  val showUploadIsDone = (event: ProgressEvent) => {
                    logger.info("Transfer ended")

                    progressText.textContent = "Upload finished ✓"
                    hide(progressBar)
                  }

                  FileUploadCallbacks(
                    onLoadStart = showInitialProgress,
                    onProgress = updateProgressInfo,
                    onLoadEnd = showUploadIsDone,
                    onError = (errorEvent: ErrorEvent) =>
                      progressText.textContent = s"Transfer failed: ${JSON.stringify(errorEvent)}"
                  )
                }
                )
            )
        ).fold(
        errorMsg => FileUploadCallbacks(onLoadStart = (_: js.Any) =>
          logger.warn(s"Could not get element for showing file upload progress: $errorMsg")),
        callbacks => callbacks)

    }

    /**
      * Gets the first file of the input element.
      *
      * @param input the input element that may contain files
      * @return optionally, the first [[File]] inside the `input`
      */
    def getFirstFile(input: Input): Option[File] = {
      toList(input.files).headOption
    }

    def uploadFile(input: Input): Unit =
      getFirstFile(input)
        .fold(logger.warn("Could not get first file from file input")) {

          fileToUpload =>
            postInFormAsync(
              FileUploadElements.Names.fileInForm,
              AppPaths.uploadFileAction,
              fileToUpload,
              callbacks
            )

        }
  }
}

