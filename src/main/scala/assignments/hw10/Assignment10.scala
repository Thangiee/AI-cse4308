package assignments.hw10

import assignments.hw9.DataSet

import scala.io.Source
import scala.util.Try

object Assignment10 extends App {

  def parseLine(line: String): DataSet = {
    line.split("\\s+").toSeq match {
      case attrs :+ label => DataSet(label.toInt, attrs.flatMap(attr => Try(attr.toDouble).toOption))
    }
  }

  val training = Source.fromFile("yeast_training.txt").getLines().map(parseLine).toVector

  val hist = for {
    c <- training.map(_.label).distinct
    a <- training.head.attrs.indices
  } yield {
//    Histogram(c, a, 4, training)
    Gaussian(c, a, training)
  }

  hist.sorted.foreach(println)
}
