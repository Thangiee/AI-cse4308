package assignments.hw4

import assignments.BaseSpec

class InferenceEngineSpec extends BaseSpec {

  val tt = Map("p" -> true, "q" -> true)
  val tf = Map("p" -> true, "q" -> false)
  val ft = Map("p" -> false, "q" -> true)
  val ff = Map("p" -> false, "q" -> false)

  "plTrue for And" should "yield the correct truth table" in {
    val expr = And(Seq(Symbol("p"), Symbol("q")))

    InferenceEngine.plTrue(expr, tt) shouldEqual true
    InferenceEngine.plTrue(expr, tf) shouldEqual false
    InferenceEngine.plTrue(expr, ft) shouldEqual false
    InferenceEngine.plTrue(expr, ff) shouldEqual false
  }

  "plTrue for Or" should "yield the correct truth table" in {
    val expr = Or(Seq(Symbol("p"), Symbol("q")))

    InferenceEngine.plTrue(expr, tt) shouldEqual true
    InferenceEngine.plTrue(expr, tf) shouldEqual true
    InferenceEngine.plTrue(expr, ft) shouldEqual true
    InferenceEngine.plTrue(expr, ff) shouldEqual false
  }

  "plTrue for Xor" should "yield the correct truth table" in {
    val expr = Xor(Seq(Symbol("p"), Symbol("q")))

    InferenceEngine.plTrue(expr, tt) shouldEqual false
    InferenceEngine.plTrue(expr, tf) shouldEqual true
    InferenceEngine.plTrue(expr, ft) shouldEqual true
    InferenceEngine.plTrue(expr, ff) shouldEqual false
  }

  "plTrue for If" should "yield the correct truth table" in {
    val expr = If(Symbol("p"), Symbol("q"))

    InferenceEngine.plTrue(expr, tt) shouldEqual true
    InferenceEngine.plTrue(expr, tf) shouldEqual false
    InferenceEngine.plTrue(expr, ft) shouldEqual true
    InferenceEngine.plTrue(expr, ff) shouldEqual true
  }

  "plTrue for Iff" should "yield the correct truth table" in {
    val expr = Iff(Symbol("p"), Symbol("q"))

    InferenceEngine.plTrue(expr, tt) shouldEqual true
    InferenceEngine.plTrue(expr, tf) shouldEqual false
    InferenceEngine.plTrue(expr, ft) shouldEqual false
    InferenceEngine.plTrue(expr, ff) shouldEqual true
  }
}
