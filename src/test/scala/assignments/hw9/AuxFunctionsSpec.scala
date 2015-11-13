package assignments.hw9

import assignments.BaseWordSpec
import org.scalacheck.Gen
import org.scalactic.TolerantNumerics

import scala.util.Random

class AuxFunctionsSpec extends BaseWordSpec {

  implicit val doubleEq = TolerantNumerics.tolerantDoubleEquality(.001)

  "Entropy" when {
    val A = 0
    val B = 1
    "given two classes with 200 A class and 200 B class must yield 1.0" in {
      val data = List.fill(200)(DataSet(A)) ++ List.fill(200)(DataSet(B))
      entropy(data) shouldEqual 1.0
    }
    "given two classes with 20 A class and 500 B class must yield .235" in {
      val data = List.fill(20)(DataSet(A)) ++ List.fill(500)(DataSet(B))
      entropy(data) shouldEqual .235
    }
    "given two classes with 20 A class and 5000 B class must yield .038" in {
      val data = List.fill(20)(DataSet(A)) ++ List.fill(5000)(DataSet(B))
      entropy(data) shouldEqual .038
    }
    "given two classes with 0 A class and 200 B class must yield .038" in {
      val data = List.fill(0)(DataSet(A)) ++ List.fill(200)(DataSet(B))
      entropy(data) shouldEqual 0.0
    }
    "given two classes should be no greater than 1.0" in {
      val range = Gen.choose(0, 1000)
      forAll((range, "sizeA"), (range, "sizeB")) { (sizeA, sizeB) =>
        val data = List.fill(sizeA)(DataSet(A)) ++ List.fill(sizeB)(DataSet(B))
        entropy(data) should be <= 1.0
      }
    }
  }

  it should {
    "always be positive" in {
      forAll("data") { labels: List[Int] =>
        val data = labels.map(l => DataSet(l % 10))
        entropy(data) should be >= 0.0
      }
    }
    "yield a largest value of log_2(N) (N=number of classes)" in {
      forAll("data") { labels: List[Int] =>
        val data = labels.map(l => DataSet(l % 10))
        val N = data.distinct.size
        entropy(data) should be <= (if (N == 0) 0 else math.log(N) / math.log(2))
      }
    }
  }

  "Information Gain" should {
    "yield approximately .38 for the given population" in {
      // http://homes.cs.washington.edu/~shapiro/EE596/notes/InfoGain.pdf
      val Green = 200
      val Plus  = 100
      val pop =
        List.fill(13)(DataSet(label = Plus, attrs = Seq(1))) ++
        List.fill(4)(DataSet(label = Green, attrs = Seq(1))) ++
        List.fill(1)(DataSet(label = Plus, attrs = Seq(2))) ++
        List.fill(12)(DataSet(label = Green, attrs = Seq(2)))

      informationGain(pop)(_.attrs.head == 1) shouldEqual (.38 +- .01)
    }
  }

  "distribution" should {
    "yield an collection, whose i-th position is the probability of the  i-th class/label" in {
      val data =
        List.fill(35)(DataSet(label = 0)) ++
        List.fill(22)(DataSet(label = 1)) ++
        List.fill(15)(DataSet(label = 2)) ++
        List.fill(37)(DataSet(label = 3)) ++
        List.fill(12)(DataSet(label = 4))

      val res = distribution(data)
      res(0) shouldEqual 0.2893
      res(1) shouldEqual 0.1818
      res(2) shouldEqual 0.1240
      res(3) shouldEqual 0.3058
      res(4) shouldEqual 0.0992

      val res2 = distribution(Random.shuffle(data))
      res2(0) shouldEqual 0.2893
      res2(1) shouldEqual 0.1818
      res2(2) shouldEqual 0.1240
      res2(3) shouldEqual 0.3058
      res2(4) shouldEqual 0.0992
    }
  }
}
