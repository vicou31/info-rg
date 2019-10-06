package client.pages.memory

import japgolly.scalajs.react._
import japgolly.scalajs.react.vdom.html_<^._

import shared.resources.path.Images.Memory._

import shared.resources.css.{MemoryStyles => style}
import scalacss.ScalaCssReact._

import scala.util.Random.shuffle

object Card{

  case class card(id: Int, display: cardDisplay, handleClick: Callback)

  def generateCards(N: Int, clickHandler: Int => Callback)= {
    if(N == 0) List.empty
    else shuffle(shuffle(List.range(0, 20)).take(N / 2).flatMap((c: Int) => List(c, c)))
      .zipWithIndex.map({ case (id, index) => card(id, down, clickHandler(index))  })
  }

  sealed trait cardDisplay
  case object out extends cardDisplay
  case object down extends cardDisplay
  case object up extends cardDisplay

  implicit val cardDisplayReuse = Reusability.by_==[cardDisplay]
  implicit val cardReuse = Reusability.by((_: card).display)

  val cardView = ScalaComponent.builder[card]("card")
    .render_P(card => <.img(
      style.card,
      card.display match {
        case `out` => TagMod(^.src := cardBackPath,^.visibility:="hidden")
        case `down` => ^.src := cardBackPath
        case `up` => ^.src := cardFacePath(card.id)
      },
      ^.alt := s"carte missing",
      ^.onClick --> card.handleClick
    )
    )
    .configure(Reusability.shouldComponentUpdate)
    .build
}




