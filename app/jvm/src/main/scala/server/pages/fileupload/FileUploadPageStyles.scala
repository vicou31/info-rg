package server.pages.fileupload

import server.pages.styles.{CssValues, SharedStyles}
import server.resources.AppImages

import scalatags.Text.all._

/**
  * Contains the styles of the HTML elements on the page for uploading files.
  * <p>
  * Created by Matthias Braun on 8/23/17.
  */
object FileUploadPageStyles extends SharedStyles {

  initStyleSheet()

  val fileLabelStyle = cls(
    fontSize := "160%",
    marginTop := CssValues.em(1)
  )

  val fileLabelClasses = s"${fileLabelStyle.name} ${pureButton.v}"

  val fileInputStyle = cls(
    // This makes the input invisible. We'll use a label instead as the button
    width := "0.1px",
    height := "0.1px",
    opacity := 0,
    overflow := "hidden",
    position := "absolute",
    zIndex := -1
  )

  val fileInfoBox =
    cls(
      // A translucent orchid
      backgroundColor := CssValues.rgba(218, 112, 214, 0.2),
      color := "darkslategray",
      display.`inline-block`,
      padding := "10px 10px 10px 10px",
      marginTop := "5px"
    )

  val progressInfoBox = cls(
    background := CssValues.rgba(255, 0, 0, 0.1),
    color := "darkslategray",
    display.`inline-block`,
    padding := "10px 10px 10px 10px",
    marginTop := "5px"
  )

  val bodyStyle = cls(
    backgroundImage := CssValues.url(AppImages.uploadFilePageBackground),
    defaultPagePadding,
  )
}
