package assignments.hw8

object Task3 extends App {

  implicit class Negation(i: Double) {
    def unary_~ = 1 - i
  }

  def P(event: Char, b: Boolean*): Double = (event, b) match {
    case ('B', Nil)               =>.001
    case ('E', Nil)               =>.002
    case ('A', Seq(true, true))   =>.95
    case ('A', Seq(true, false))  =>.94
    case ('A', Seq(false, true))  =>.29
    case ('A', Seq(false, false)) =>.001
    case ('J', Seq(true))         =>.90
    case ('J', Seq(false))        =>.05
    case ('M', Seq(true))         =>.70
    case ('M', Seq(false))        =>.01
    case _                        => 0.0
  }

  def parseEvent(s: String): Event = Event(s.head, if (s.last == 't') true else false)

  case class Event(name: Char, truthful: Boolean)

  def calcPrFromAllEvents(allEvents: Seq[Event]): Double = {
    allEvents.view.map {
      case Event('B', truthful) => (P('B'), truthful)
      case Event('E', truthful) => (P('E'), truthful)
      case Event('A', truthful) => (P('A', allEvents.find(_.name == 'B').get.truthful, allEvents.find(_.name == 'E').get.truthful), truthful)
      case Event('J', truthful) => (P('J', allEvents.find(_.name == 'A').get.truthful), truthful)
      case Event('M', truthful) => (P('M', allEvents.find(_.name == 'A').get.truthful), truthful)
    }.map {
      case (p, truthful) => if (!truthful) ~p else p
    }.product
  }

  def genAllEvents(givenEvents: Seq[Event]): Seq[Seq[Event]] = {
    val missingEventsName = List('B', 'E', 'A', 'J', 'M') diff givenEvents.map(_.name)
    val n = missingEventsName.size

    val missingEvents = truthTable(n).map { tt =>
      (0 until n).map(i => Event(missingEventsName(i), tt(i)))
    }

    missingEvents.map(_ ++ givenEvents)
  }

  def truthTable(n: Int): Seq[Seq[Boolean]] = {
    if (n < 1) List(Nil)
    else {
      val subtable = truthTable(n - 1)
      for {
        row <- subtable
        v <- List(true, false)
      } yield row :+ v
    }
  }

  val givenIndex = args.indexWhere(_ == "given")
  val numerator = args.filter(_ != "given").toSeq
  val denominator = if (givenIndex == -1) Seq.empty[String] else args.splitAt(givenIndex+1)._2.toSeq

  val a = genAllEvents(numerator.map(parseEvent)).map(calcPrFromAllEvents).sum
  if (denominator.nonEmpty) {
    val b = genAllEvents(denominator.map(parseEvent)).map(calcPrFromAllEvents).sum
    println(f"${a / b}%.10f")
  } else {
    println(f"$a%.10f")
  }
}
