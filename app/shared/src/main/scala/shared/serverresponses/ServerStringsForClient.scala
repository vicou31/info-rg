package shared.serverresponses

import shared.data.Person
import upickle.default.{ReadWriter => RW, macroRW}


/**
  * Contains the strings the server sends to the client.
  * <p>
  * Created by Matthias Braun on 1/4/2017.
  */
object ServerStringsForClient {


  private def personOrPersons(nrOfPersons: Int): String = if (nrOfPersons == 1) "person" else "persons"

  def gotPersons(persons: Seq[Person]): ServerStringForClient = {
    val nrOfPersons = persons.size
    ServerStringForClient(s"Thanks client, I got $nrOfPersons ${personOrPersons(nrOfPersons)}")
  }

  def errorWhileSavingPersons(persons: Seq[Person], error: Throwable) =
    ServerStringForClient(s"Got persons but error occurred while saving them in the database. Persons $persons. Error: $error")

}

case class ServerStringForClient(value: String) extends AnyVal {
  override def toString: String = value
}

object ServerStringForClient{
  implicit val rw: RW[ServerStringForClient] = macroRW
}
