package client.pages

import org.scalajs.dom._
import shared.api.AppPaths

import scalatags.JsDom.all._

/**
  * This code will run as JavaScript on the welcome page.
  * <p>
  * Created by Matthias Braun on 1/5/2017.
  */
object WelcomePageScript extends ClientScriptHelper {

  private def onPageLoaded() = bodyAppend(
    div(
      p("The ", b("client"), " added this with JavaScript. Or was it Scala? After upgrade 😊"),
      a(href := AppPaths.exampleOverviewPage)("Show me something more involved.")
    )
  )

  /**
    * Execute the client code on the page when it's loaded so the dynamic content is
    * added after the content provided by the server.
    */
  def execute(): Any = window.onload = e => onPageLoaded()
}
