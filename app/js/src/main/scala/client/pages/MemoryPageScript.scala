package client.pages

import org.scalajs.dom.{document, window}
import client.pages.memory.App
import shared.resources.css.CssSettings._
import shared.resources.css.MemoryStyles
import scalacss.ScalaCssReact._

object MemoryPageScript extends ClientScriptHelper {
  def execute() =
    window.onload = _ => {
      MemoryStyles.addToDocument()
      App.appView().renderIntoDOM(
        document.getElementById("app")
      )
    }

}
