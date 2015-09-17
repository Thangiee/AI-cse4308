package assignments.hw3

import assignments.BaseSpec

class AiSpec extends BaseSpec {

  "findBestPlay" should "pass these sanity tests" in {
    val gb = new GameBoard(Array(
      Array(0, 0, 0, 0, 0, 0, 0),
      Array(0, 0, 0, 0, 0, 0, 0),
      Array(0, 0, 0, 0, 0, 0, 0),
      Array(0, 0, 0, 0, 1, 0, 0),
      Array(0, 0, 0, 0, 2, 0, 0),
      Array(1, 1, 0, 2, 2, 2, 1)
    ))

    Ai.findBestPlay(gb, 3) shouldEqual 2

    val gb2 = new GameBoard(Array(
      Array(0, 0, 0, 0, 0, 0, 0),
      Array(0, 0, 0, 0, 0, 0, 0),
      Array(2, 0, 0, 0, 0, 0, 0),
      Array(1, 1, 0, 0, 0, 0, 0),
      Array(1, 1, 0, 0, 0, 0, 0),
      Array(2, 2, 0, 2, 2, 1, 0)
    ))
    Ai.findBestPlay(gb2, 3) shouldEqual 2

    val gb3 = new GameBoard(Array(
      Array(0, 1, 0, 0, 0, 0, 0),
      Array(1, 1, 0, 1, 0, 0, 0),
      Array(2, 2, 0, 2, 0, 1, 0),
      Array(2, 1, 0, 2, 0, 2, 0),
      Array(1, 1, 1, 1, 0, 1, 2),
      Array(1, 2, 1, 2, 2, 2, 2)
    ))
    Ai.findBestPlay(gb3, 5) shouldEqual 4
  }
}
