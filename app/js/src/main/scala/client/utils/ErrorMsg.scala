package client.utils

/**
  * An error message used for when something unexpected happened in this application.
  * <p>
  * Created by Matthias Braun on 1/7/2017.
  */
case class ErrorMsg(value: String) extends AnyVal {
  override def toString: String = value
}
