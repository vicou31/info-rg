package shared.pages.elements

/**
  * The ID of an HTML element.
  * <p>
  * Created by Matthias Braun on 1/6/2017.
  */
case class HtmlId(value: String) extends AnyVal {
  override def toString: String = value
}
