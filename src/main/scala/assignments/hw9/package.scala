package assignments

package object hw9 {

  def chooseAttr(examples: Seq[DataSet], testAttrs: Seq[Double]): (Double, Double) = ???

  def distribution(examples: Seq[DataSet]): Seq[Double] = ???

  def entropy(data: Seq[DataSet]): Double = {
    def log2(x: Double): Double = if (x == 0) 0 else math.log(x) / math.log(2)

    if (data.isEmpty) 0
    else {
      val k  = data.size
      val kN = data.groupBy(_.label).values
      kN.view.map(_.size.toDouble).map(ki => -(ki/k) * log2(ki/k)).sum
    }
  }

  def informationGain(data: Seq[DataSet])(threshold: DataSet => Boolean): Double = {
    val (p1, p2) = data.partition(threshold)
    val k = data.size.toDouble

    entropy(data) - p1.size/k * entropy(p1) - p2.size/k * entropy(p2)
  }
}
