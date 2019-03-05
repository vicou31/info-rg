package shared.serverresponses


import upickle.default.{ReadWriter => RW, macroRW}

/**
  * The client receives one of these responses from the server after sending it data or requesting data.
  * <p>
  * Created by Matthias Braun on 1/9/2017.
  */
sealed trait ServerResponse

case class GotPersonsAndSavedInDb(msg: ServerStringForClient) extends ServerResponse

case class GotPersonsButErrorWhileSaving(msg: ServerStringForClient) extends ServerResponse

object ServerResponse{
  implicit val rw: RW[ServerResponse] = macroRW
}
object GotPersonsAndSavedInDb{
  implicit val rw: RW[GotPersonsAndSavedInDb] = macroRW
}
object GotPersonsButErrorWhileSaving{
  implicit val rw: RW[GotPersonsButErrorWhileSaving] = macroRW
}