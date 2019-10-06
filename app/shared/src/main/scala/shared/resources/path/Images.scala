package shared.resources.path

/**
  * Provides paths to the memory of this applications.
  * <p>
  * Created by Matthias Braun on 1/13/2017.
  */
object Images {

  private val imageDir = "images"

  object Memory {
    val cardBackPath = s"$imageDir/memory/dos.jpg"
    def cardFacePath(i:Int) = s"$imageDir/memory/cartes/img$i.jpg"
    def cardVictoryPath(i:Int) = s"$imageDir/memory/victoire$i.jpg"
  }






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
