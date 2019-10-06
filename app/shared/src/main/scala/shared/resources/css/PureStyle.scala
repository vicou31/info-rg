package shared.resources.css

import scalacss.internal.mutable.StyleSheet
import CssSettings._


trait PureStyle extends StyleSheet.Inline {

  import dsl._

  val domainDiv = Domain.ofValues("centered")
  val pureDiv = styleF(domainDiv)(
    alignement => styleS(
      addClassName("pure-u"),
      alignement match {
        case "centered" => alignContent.center
        case _ => alignContent.flexStart
      }
    )
  )

  val gridSize = styleF.int(0 to 100)(
    size => styleS(
      addClassName(s"pure-u-1-$size")
    )

  )

  val pureGrid = style(
    addClassName("pure-g")
  )
  val domainButton = Domain.ofValues("primary")

  val pureButton = styleF(domainButton)(
    buttontype => styleS( addClassName(s"pure-button pure-button-$buttontype"))
  )

  val pureImage = style(
    addClassName("pure-img")
  )

  val pureForm = style(
    addClassName("pure-form")
  )

  val pureFormStack = style(
    addClassName("pure-form pure-form-stacked")
  )

}
