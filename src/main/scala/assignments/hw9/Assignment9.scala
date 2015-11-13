package assignments.hw9

import scala.io.Source
import scala.util.Random._
import scala.util.Try

object Assignment9 extends App {

  def parseLine(line: String): DataSet = {
    line.split("\\s+").toSeq match {
      case attrs :+ label => DataSet(label.toInt, attrs.flatMap(attr => Try(attr.toDouble).toOption))
    }
  }

  def findBestLabel(labelsDistro: Distribution): Int = {
    // shuffle in case of a tie in which a random label will be selected from one of the tied labels
    val (bestLabel, _) = shuffle(labelsDistro.toSeq).maxBy { case (label, distro) => distro }
    bestLabel
  }

  def calcAccuracy(realLabel: Int, labelsDistro: Distribution): Double = {
    labelsDistro.keys.toSeq match {
      case prediction :: Nil => if (prediction == realLabel) 1.0 else 0.0
      case predictions       =>
        val (bestLabel, bestDistro) = shuffle(labelsDistro.toSeq).maxBy { case (label, distro) => distro }
        if (realLabel == bestLabel) 1.0/labelsDistro.find(_._2 == bestDistro).size else 0.0
    }
  }

  def avgDistro(distributions: Seq[Distribution]): Distribution = {
    val n = distributions.size
    val mergedDistro = distributions.map(_.toSeq).reduce(_ ++ _).groupBy(_._1).mapValues(_.map(_._2).toSeq)
    mergedDistro.map { case (label, probabilities) => (label, probabilities.sum/n) }
  }

  val training = Source.fromFile(args(0)).getLines().map(parseLine).toVector
  val testing  = Source.fromFile(args(1)).getLines().map(parseLine).toVector

  val dts = args(2) match {
    case "optimized" => Seq(DecisionTree.learn(0, training, training.head.attrs.indices, Optimized, pruning = true))
    case "randomized" => Seq(DecisionTree.learn(0, training, training.head.attrs.indices, Randomized, pruning = true))
    case "forest3" => (0 until 3).map(id => DecisionTree.learn(id, training, training.head.attrs.indices, Randomized, pruning = true))
    case "forest15" => (0 until 15).map(id => DecisionTree.learn(id, training, training.head.attrs.indices, Randomized, pruning = true))
    case _ => throw new IllegalArgumentException("possible values: optimized, randomized, forest3, or forest15")
  }

  dts.foreach(println)

  val accuracies = testing.zipWithIndex.map {
    case (test, i) =>
      val labelsAvgDistro = avgDistro(dts.map(dt => dt.classify(test)))
      val predicted = findBestLabel(labelsAvgDistro)
      val accuracy = calcAccuracy(test.label, labelsAvgDistro)
      println(f"ID=$i%5d, predicted=$predicted%3d, true=${test.label}%3d, accuracy=$accuracy%4.2f")
      accuracy
  }

  println(f"classification accuracy=${accuracies.sum/accuracies.size}%6.4f")

}
