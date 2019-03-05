package shared.pages.elements.classes

/**
  * The class of an HTML element.
  * <p>
  * Created by Matthias Braun on 5/30/2017.
  */
case class HtmlClass(value: String) extends AnyVal {
  override def toString: String = value
}
