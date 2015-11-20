package assignments.hw10

import assignments.hw9.DataSet

// Mixture of Gaussians
case class Mixture(gaussians: Seq[Gaussian]) {

  def pdf() = ???
}

object Mixture {

  def apply(label: Int, attr: Int, trainingData: Seq[DataSet], sizeOfMixture: Int): Mixture = {

    val attrVals = trainingData.filter(_.label == label).map(_.attrs(attr)) // todo: all?
    val S = attrVals.min
    val L = attrVals.max
    val G = (L-S) / sizeOfMixture

    // initialize
    var gaussians = (1 to sizeOfMixture).map(N => Gaussian(label, attr, mean = S + (N-1)*G + G/2, std = 1))

    // fit a mixture of gaussians to data using EM-algo
    for (i <- 0 until 50) {
      gaussians = gaussians.map { gauss =>

        val mean = 0.0
        val std = 0.0
        val weight = 1.0

        gauss.copy(mean = mean, std = std, weight = weight)
      }
    }

    Mixture(gaussians)
  }

}
