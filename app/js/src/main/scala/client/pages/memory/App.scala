package client.pages.memory

import japgolly.scalajs.react._
import japgolly.scalajs.react.vdom.html_<^._
import scalacss.ScalaCssReact._
import org.scalajs.dom.raw.HTMLInputElement
import shared.resources.css.{MemoryStyles => style}


object App {

  val appView = ScalaComponent.builder[Unit]("appComponent")
    .initialState(States.config(0))
    .renderBackend[appBackend]
    .build

  class appBackend($ : BackendScope[Unit, States.config]){

    object Validator{
      def numberOfCardsInput(e: ReactEventFromInput) = {

          val inputValue: Int = e.target.valueAsNumber.toInt
          if(inputValue % 2 != 0 || inputValue <= 0 || inputValue > 40)
            Callback.alert("Entrer un nombre pair de carte entre 2 et 40") >> FormRef.numberOfCards.get.map(input => {
              input.value=""
              input.focus()
            })
          else {
            $.modState(_.copy(numberOfcards = inputValue))
          }
        }

      def timeDisplayInput(e: ReactEventFromInput) = {
          val inputValue: Int = e.target.valueAsNumber.toInt
          if(inputValue <=0 || inputValue> 10000)
            Callback.alert(
              s"You will be waiting for ${inputValue/1000} seconds until the cards are flipped back. It's LONG"
            )
          else {
            $.modState(_.copy(timeDisplay = inputValue))
          }
        }
      }

    object FormRef{
      val numberOfCards = Ref[HTMLInputElement]
      val timeToDisplay = Ref[HTMLInputElement]
    }

    object Call{
      def formSubmit(e: ReactEventFromInput) = Callback{e.preventDefault()} >> $.modState(
        _.copy(
         numberOfcards = FormRef.numberOfCards.unsafeGet.valueAsNumber.toInt,
         timeDisplay = FormRef.timeToDisplay.unsafeGet.valueAsNumber.toInt
        )
      )
    }

    def render(config:States.config)= <.div(style.appContainer)(
      <.form(style.pureFormStack)(
        <.fieldset(
           <.legend(
            "Choose a number of cards for playing."
          ),
          <.div(style.pureGrid)(
            <.div(style.gridSize(3))(
              <.label(^.`for` := "inputCards")("Number of cards :"),
              <.input(
                ^.margin:="0 auto",
                ^.id := "inputCards",
                ^.`type` := "number",
                ^.placeholder := " between 2 et 40",
                ^.onBlur ==> Validator.numberOfCardsInput,
              ).withRef(FormRef.numberOfCards)
            ),
            <.div(style.gridSize(3))(
              <.label(^.`for` := "inputTime")("Time to display: (default 1000)"),
              <.input(
                ^.margin:="0 auto",
                ^.id := "inputTime",
                ^.`type` := "number",
                ^.placeholder := " in millis",
                ^.onBlur ==> Validator.timeDisplayInput
              ).withRef(FormRef.timeToDisplay)
            ),
            <.div(style.gridSize(3))(
              <.label(^.`for` := "startButton")("Ready to start?"),
              <.button(^.id := "startButton",^.margin:="0 auto", style.pureButton("primary"), ^.onClick ==> Call.formSubmit)("Start")
            )
          )
        )
      ),
      Board.boardView(config)
    )



  }

  val scoreView = ScalaComponent.builder[Int]("score")
    .render_P(score => <.button(^.id := "score", style.pureButton("primary"))(
      s"Score : ${score}"
    )
    )
    .configure(Reusability.shouldComponentUpdate)
    .build

}

