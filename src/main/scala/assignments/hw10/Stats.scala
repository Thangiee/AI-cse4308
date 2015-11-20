package assignments.hw10

object Stats {

  def mean[A](xs: Seq[A])(implicit num: Numeric[A]): Double = num.toDouble(xs.sum) / xs.size

  def devs[A](xs: Seq[A])(implicit num: Numeric[A]): Seq[Double] = {
    val mean = xs.mean
    xs.map(num.toDouble).map(x => Math.pow(x - mean, 2))
  }

  def stddev[A](xs: Seq[A])(implicit num: Numeric[A]): Double = Math.sqrt(xs.devs.sum / xs.size)
}
