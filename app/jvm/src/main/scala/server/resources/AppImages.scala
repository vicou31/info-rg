package server.resources

/**
  * Provides paths to the images of this applications.
  * <p>
  * Created by Matthias Braun on 1/13/2017.
  */
object AppImages {


  val imageDir = "images"
  val backgroundDir = s"$imageDir/backgrounds"
  val pageImageDir = s"$imageDir/pageimages"

  val favicon = s"$imageDir/favicon.ico"

  val pentagonBackground = s"$backgroundDir/congruent_pentagon.png"
  val uploadFilePageBackground = s"$backgroundDir/memphis_colorful.png"
  val examplesOverviewPageBackground = s"$backgroundDir/hip_square.png"

  val peopleFormGif = s"$pageImageDir/people_form.gif"
  val drawOnCanvasGif = s"$pageImageDir/draw_on_canvas.gif"
  val uploadFileGif = s"$pageImageDir/upload_file.gif"
  val chunkStreamGif = s"$pageImageDir/chunk_stream.gif"
}
