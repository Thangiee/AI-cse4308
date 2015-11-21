package assignments.hw10

import assignments.hw9.DataSet

// Mixture of Gaussians
case class Mixture(label: Int, attr: Int, gaussians: Seq[Gaussian]) {

  def pdf() = ???

  override def toString: String = gaussians.zipWithIndex.map { case (g, i) =>
    f"Class $label, attribute $attr, Gaussian ${i+1}, mean = ${g.mean}%.2f, std = ${g.std}%.2f"
  }.mkString("\n")
}

object Mixture {

  implicit val mixtureOrdering = Ordering.by { m: Mixture => (m.label, m.attr) }

  def apply(label: Int, attr: Int, trainingData: Seq[DataSet], sizeOfMixture: Int): Mixture = {

    val S = trainingData.map(_.attrs(attr)).min
    val L = trainingData.map(_.attrs(attr)).max
    val G = (L-S) / sizeOfMixture

    var gaussians = (1 to sizeOfMixture).map(N => Gaussian(label, attr, mean = S + (N-1)*G + G/2, std = 1))

    // fit a mixture of gaussians to data using EM-algo
    gaussians = gaussians.map { gauss =>
      var attrVals = trainingData.filter(_.label == label).map(_.attrs(attr))
      var newGauss = gauss

      for (i <- 0 until 50) {
        val updatedAttrVals = attrVals.map(value => newGauss.pdf(value))

        val uj = attrVals.mean
        val std = math.sqrt(attrVals.map(value => newGauss.pdf(value)*(value*uj).^(2)).sum / attrVals.map(value => newGauss.pdf(value)).sum)
        println(std)

        if (std == 0) {
          newGauss = newGauss.copy(mean = 1, std = 0, weight = 1)
        } else {
          val mean = attrVals.map(value => newGauss.pdf(value)*value).sum / attrVals.map(value => newGauss.pdf(value)).sum
          val weight = attrVals.sum / gaussians.map(g => attrVals.map(x => g.pdf(x)).sum).sum
          attrVals = updatedAttrVals
          newGauss = newGauss.copy(mean = mean, std = std, weight = weight)
        }
      }

      newGauss
    }

    Mixture(label, attr, gaussians)
  }

}
