package shared.utils

/**
  * Helps with [[Eithers]]
  * <p>
  * Created by Matthias Braun on 1/7/2017.
  */
object Eithers {

  /**
    * Turns a `Seq[Either[L, R]]` into an Either[L, Seq[R].
    * We either concatenate all the Rs or return the first L.
    *
    * @param s turn this `Seq[Either[L, R]]` into an `Either[L, Seq[R]]`
    * @tparam L the either's left type
    * @tparam R the either's right type
    * @return either the first L or the concatenated Rs
    */
  def sequence[L, R](s: Seq[Either[L, R]]): Either[L, Seq[R]] =

    // From https://stackoverflow.com/questions/7230999/how-to-reduce-a-seqeithera-b-to-a-eithera-seqb#7231180
    s.foldRight(Right(Nil): Either[L, List[R]]) {
      (e, acc) => for (xs <- acc.right; x <- e.right) yield x :: xs
    }
}
