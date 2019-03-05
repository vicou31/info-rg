package shared.data


import upickle.default.{ReadWriter => RW, macroRW}

/**
  * Represents a point that was drawn on a canvas.
  * <p>
  * Created by Matthias Braun on 5/3/2017.
  */
case class DrawnPoint(x: Double, y: Double)

object DrawnPoint{
  implicit val rw: RW[Person] = macroRW
}
