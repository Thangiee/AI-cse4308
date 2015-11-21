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

  val training = Source.fromFile(args(0)).getLines().map(parseLine).toVector
  val testing  = Source.fromFile(args(1)).getLines().map(parseLine).toVector


  args(2) match {
    case "histograms" =>
      val k = args(3)

      val hists = (for {
        c <- training.map(_.label).distinct
        a <- training.head.attrs.indices
      } yield {
        Histogram(c, a, k.toInt, training)
      }).sorted

      hists.foreach(println)

      val classes = hists.map(_.label).distinct

      val accuracies = testing.zipWithIndex.map { case (test, i) =>
        val res = classes.map { c =>
          (c, (hists.filter(_.label == c) zip test.attrs).map { case (hist, attr) => hist.Pr(attr) }.sum / classes.size)
        } maxBy (_._2)
        val (predicted, pr) = res
        val acc = if (predicted == test.label) 1 else 0

        println(f"ID=$i%5d, predicted=$predicted%3d, probability = $pr%.4f, true=${test.label}%3d, accuracy=${acc.toDouble}%4.2f")
        acc
      }
      println(f"classification accuracy=${accuracies.mean}%6.4f\n")

    case "gaussians" =>
      val gaussians = (for {
        c <- training.map(_.label).distinct
        a <- training.head.attrs.indices
      } yield {
        Gaussian(c, a, training)
      }).sorted

      gaussians.foreach(println)
      println("Did not implement gaussian classification")

    case "mixtures" =>
      println("Did not implementing mixtures")
  }

}
