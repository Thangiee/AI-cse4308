package assignments.hw8

import scala.io.Source
import scala.util.Try

object Task2 extends App {

  val data = Source.fromFile("training_data.txt").getLines().map(parseLine).toVector

  // column index for events in training_data.txt
  val GameOn     = 0
  val WatchTV    = 1
  val OutCatFood = 2
  val FeedCat    = 3

  def parseLine(line: String) = line.split("     ").flatMap(s => Try(s.toInt).toOption)

  def count(conditions: (Int, Int)*): Double = data.count(row => conditions.forall { case (event, value) => row(event) == value })

  println(s"P(baseball_game_on_TV) = ${count(GameOn -> 1) / data.size}")
  println(s"P(out_of_cat_food) = ${count(OutCatFood -> 1) / data.size}")

  println(s"P(George_watches_TV | baseball_game_on_TV) = ${ count(GameOn -> 1, WatchTV -> 1) / count(GameOn -> 1) }")
  println(s"P(George_watches_TV | ¬baseball_game_on_TV) = ${ count(GameOn -> 0, WatchTV -> 1) / count(GameOn -> 0) }")

  println("P(George_feeds_cat | George_watches_TV, out_of_cat_food) = " +
    count(FeedCat -> 1, WatchTV -> 1, OutCatFood -> 1) / count(WatchTV -> 1, OutCatFood -> 1))

  println("P(George_feeds_cat | George_watches_TV, ¬out_of_cat_food) = " +
    count(FeedCat -> 1, WatchTV -> 1, OutCatFood -> 0)  / count(WatchTV -> 1, OutCatFood -> 0))

  println("P(George_feeds_cat | ¬George_watches_TV, out_of_cat_food) = " +
    count(FeedCat -> 1, WatchTV -> 0, OutCatFood -> 1)  / count(WatchTV -> 0, OutCatFood -> 1))

  println("P(George_feeds_cat | ¬George_watches_TV, ¬out_of_cat_food) = " +
    count(FeedCat -> 1, WatchTV -> 0, OutCatFood -> 0)  / count(WatchTV -> 0, OutCatFood -> 0))
}
