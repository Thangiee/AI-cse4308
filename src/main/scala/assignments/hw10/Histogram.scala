package assignments.hw10

import assignments.hw9.DataSet

import scala.util.Try

case class Histogram(label: Int, attr: Int, numOfBins: Int, trainingData: Seq[DataSet]) {
  private val attrVals = trainingData.filter(_.label == this.label).map(_.attrs(attr))
  private val N = numOfBins
  private val S = attrVals.min
  private val L = attrVals.max
  private val G = (L-S) / N

  private val bins = (0 until N).map {
    case 0 => attrVals.filter(value => Double.NegativeInfinity < value && value <= S+G)
    case n => attrVals.filter(value => S+n*G < value && value <= S+(n+1)*G)
  }

  private val probabilities = bins.map(_.size.toDouble / attrVals.size)

  def binFrq(binNum: Int): Int = Try(bins(binNum).size).getOrElse(0)

  def binPr(binNum: Int): Double = Try(probabilities(binNum)).getOrElse(0.0)

  override def toString: String =
    bins.indices.map(i => f"Class $label, attribute $attr, bin $i, P(bin | class) = ${binPr(i)}%.2f").mkString("\n")
}

object Histogram {
  implicit val histogramOrdering = Ordering.by{ h: Histogram => (h.label, h.attr, h.numOfBins) }
}