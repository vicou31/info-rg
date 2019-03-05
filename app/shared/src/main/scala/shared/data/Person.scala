package shared.data

import java.util.UUID
import upickle.default.{ReadWriter => RW, macroRW}

case class Person(id: UUID, name: String, age: String, occupation: String) {

  /** @return true, if any of the [[Person]]'s fields contains data */
  def nonEmpty: Boolean = name.trim.nonEmpty || age.trim.nonEmpty || occupation.trim.nonEmpty
}

object Person extends ((UUID,String,String,String) => Person){
  implicit val rw: RW[Person] = macroRW
}
