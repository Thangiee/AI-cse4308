package assignments

class EdgeSpec extends BaseSpec {

  "Same edge with different cost" must "be equivalent" in {
    forAll() { (a: Int, b: Int, c: Int) =>
      Edge(a, b)(c) shouldEqual Edge(a, b)(c + 1)
    }
  }

  "reverse" must "return a new edge witch the src and dst switched" in {
    forAll() { (a: Int, b: Int, c: Int) =>
      Edge(a, b)(c).reverse shouldEqual Edge(b, a)(c)
    }
  }

}
