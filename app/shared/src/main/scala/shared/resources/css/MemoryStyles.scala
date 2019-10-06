package shared.resources.css
import CssSettings._

object MemoryStyles extends PureStyle {
  import dsl._

  val appContainer = style(
    textAlign.center
  )


  val board = style(
    width.auto,
    padding( 20 px),
    marginLeft.auto,
    marginRight.auto,
    display.block
  )

  val card = style(
    //addClassName("pure-img"),
    width(100.px),
    height(150.px),
    border(1.px, black, solid),
    margin(2.px)
  )

/*  val score = style(
    marginLeft.auto,
    marginRight.auto,
    marginBottom(10.px),
    paddingBottom(5.px),
    paddingTop(5.px),
    backgroundColor(c"#4bdbff"),
    width(100.px),
    fontSize(20.px),
    border(1.px, black, solid)
  )*/

  val victoryImg = style(
    pureImage,
    maxHeight(300.px)
  )


}
