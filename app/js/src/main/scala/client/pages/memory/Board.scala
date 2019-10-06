package client.pages.memory

import shared.resources.css.{MemoryStyles => style}
import shared.resources.path.Images.Memory._

import client.pages.memory.App.scoreView

import japgolly.scalajs.react._
import japgolly.scalajs.react.vdom.html_<^._
import scalacss.ScalaCssReact._

import org.scalajs.dom.window


object Board {
  val boardView = ScalaComponent.builder[States.config]("boardComponent")
    .initialState(States.board(width = window.innerWidth.toInt))
    .renderBackend[boardBackend]
    .componentDidMount($ => Callback{window.onresize = _ => $.modState(_.copy(width = window.innerWidth.toInt)).runNow()})
    .componentWillReceiveProps($ =>
        $.modState(_.copy(cards = Card.generateCards($.nextProps.numberOfcards,$.backend.Call.cardclick)))
      )
    .build



  class boardBackend($: BackendScope[States.config, States.board]) {

    object Call {
      val cardclick = (index: Int) => $.state >>= (s =>
        Callback.sequence(
          List(
            displayup(index),
            matchcheck(index, s).unless_(s.last == -1)
          )
        ).unless_(s.waiting || index == s.last)
        )

      /*Lors d'un click sur une carte on commence par la révéler  et on freeze le board si une carte a déjà été choisie
        sinon on met simplement à jour last*/

      val displayup = (index: Int) => $.modState(s => {
        val cardClicked = s.cards(index)
        val newcards = s.cards.updated(index,cardClicked.copy(display = Card.up))
        //s.cards.zipWithIndex.map({ case (cd:Card.card, i: Int) => if(i == index) cd.copy(display = Card.up) else cd })
        s.copy(
          cards = newcards,
          waiting = if(s.last == -1) false else true,
          last = if(s.last == -1) index else s.last
        )
      })

      /*Si deux cartes on été révélé on vérifie si elles match :
        oui -> on les sort, augmente le score de 2, defreeze le board
        non -> on les recouvre, diminue le score de 1, defreeze le board
        Dans les deux cas le callback e mise à jour de l'état du board est lancé avec delay */

      val matchcheck = (index: Int, bs: States.board) => $.modState(s => {
        val (newdisplay, newscore) = if(s.cards(s.last).id == s.cards(index).id) (Card.out, bs.score + 2) else (Card.down, bs.score - 1)
        val newcards = for((card, i) <- s.cards.zipWithIndex) yield {
          if(i == index || i == s.last) {
            card.copy(display = newdisplay)
          }
          else {
            card
          }
        }
        s.copy(cards = newcards, score = newscore, last = -1, waiting = false)
      }
      ).delayMs(1000).toCallback // convertit le callback async en simple Callback
    }


    def render(bs: States.board) = {

      def dimcol = {
        val n = bs.cards.length
        if(n!=0){
          val col = (1 to Math.sqrt(n).toInt).filter(a => n % a == 0).max
          Math.min(Math.max(col,n/col), bs.width / 120)
        }
        else 1

      }

      def victory = !bs.cards.isEmpty && bs.cards.filter(card => card.display != Card.out).isEmpty
        if(victory) {
            <.div(
              <.h2(^.textAlign:="center")(s" Félicitation ! Vous avez gagné avec un score de ${bs.score} points!"),
              <.div(^.alignContent:="centered")(
                (1 to 3).toTagMod(i => <.img( style.victoryImg , ^.src := cardVictoryPath(i) ))
            )
            )

        }
        else {
         <.div(
           scoreView(bs.score),
              <.div(^.alignContent:="centered",style.board)(
                  bs.cards.map(card => Card.cardView(card)).grouped(dimcol).toTagMod(row => <.div(row.toTagMod))
          )
          )

        }



    }





  }
}
