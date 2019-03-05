package server.pages.gui

import scalatags.Text.TypedTag
import scalatags.Text.all._
import scalatags.stylesheet.Cls

/**
  * Provides HTML buttons.
  * <p>
  * Created by Matthias Braun on 1/6/2017.
  */
object Buttons {

  def withTextAndIcon(text: String, icon: TypedTag[String], styles: Seq[Cls]): TypedTag[String] = {
    val textAndIcon = div(icon, span(s" $text"))
    button(styles)(`type` := "button")(textAndIcon)
  }

  def withTextAndIcon(text: String, icon: TypedTag[String], style: Cls): TypedTag[String] =
    withTextAndIcon(text, icon, Seq(style))

  def withText(text: String, style: Cls): TypedTag[String] = withTextAndIcon(text, div(), style)
}
