package client.pages.memory

import client.pages.memory.Card.card

object States {
  case class board(cards: List[card]=List.empty, width: Int, score: Int=0, last: Int = -1, waiting: Boolean=false)
  case class config(numberOfcards:Int, timeDisplay:Int=1000)
}