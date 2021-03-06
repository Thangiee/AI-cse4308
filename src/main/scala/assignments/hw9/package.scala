package assignments

import scala.util.Random

package object hw9 {

  type Distribution = Map[Int, Double]

  sealed trait Strategy
  case object Optimized extends Strategy
  case object Randomized extends Strategy

  case class DataSet(label: Int, attrs: Seq[Double] = Seq.empty)

  def chooseAttr(examples: Seq[DataSet], testAttrsIndices: Seq[Int], strategy: Strategy): (Int, Double, Double) = {
    var maxGain, bestAttr, bestThreshold = -1.0

    val indices = strategy match {
      case Optimized  => testAttrsIndices
      case Randomized => Seq(Random.shuffle(testAttrsIndices).head)
    }

    for (index <- indices) {
      val attrVals = examples.map(_.attrs(index))
      val l = attrVals.min
      val m = attrVals.max

      for (k <- 1 to 50) {
        val threshold = l + k * (m-l) / 51
        val gain = informationGain(examples)(_.attrs(index) < threshold)

        if (gain > maxGain) {
          maxGain = gain
          bestAttr = index
          bestThreshold = threshold
        }
      }
    }

    (bestAttr.toInt, bestThreshold, maxGain)
  }

  def distribution(examples: Seq[DataSet]): Distribution = {
    val n = examples.size.toDouble
    examples.groupBy(_.label).map{ case (label, ex) => (label, ex.size/n) }
  }

  def entropy(data: Seq[DataSet]): Double = {
    def log2(x: Double): Double = if (x == 0) 0 else math.log(x) / math.log(2)

    if (data.isEmpty) 0
    else {
      val k  = data.size
      val kN = data.groupBy(_.label).values
      kN.view.map(_.size.toDouble).map(ki => -(ki/k) * log2(ki/k)).sum
    }
  }

  def informationGain(data:  Seq[DataSet])(threshold: DataSet => Boolean): Double = {
    val (p1, p2) = data.partition(threshold)
    val k = data.size.toDouble

    entropy(data) - p1.size/k * entropy(p1) - p2.size/k * entropy(p2)
  }
}
