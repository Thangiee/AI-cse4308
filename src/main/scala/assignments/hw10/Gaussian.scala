package assignments.hw10

import assignments.hw9.DataSet

case class Gaussian(label: Int, attr: Int, mean: Double, std: Double, weight: Double = 1.0) {

  def pdf(x: Double): Double = {
    val μ = mean
    val σ = std
    val π = 3.14159265
    val e = 2.71828183

    val exponent = -(x - μ).^(2) / (2*σ.^(2))
    1 / (σ * math.sqrt(2*π)) * e.^(exponent)
  }

  override def toString: String = f"Class $label, attribute $attr, mean = $mean%.2f, std = $std%.2f"
}

object Gaussian {

  implicit val gaussianOrdering = Ordering.by { g: Gaussian => (g.label, g.attr) }

  def apply(label: Int, attr: Int, trainingData: Seq[DataSet]): Gaussian = {
    val attrVals = trainingData.filter(_.label == label).map(_.attrs(attr))
    val std = attrVals.stddev

    Gaussian(label, attr, if (std == 0) 1 else attrVals.mean , attrVals.stddev)
  }
}
