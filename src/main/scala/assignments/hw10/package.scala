package assignments

package object hw10 {

  implicit class NumberStatsOps[N: Numeric](xs: Seq[N]) {
    def mean = Stats.mean(xs)
    def devs = Stats.devs(xs)
    def stddev = Stats.stddev(xs)
  }

  implicit class PowerOp(base: Double) {
    def ^(exp: Double) = math.pow(base, exp)
  }
}
