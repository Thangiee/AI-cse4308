package assignments.hw3

import assignments.BaseSpec

class MinimaxSpec extends BaseSpec {

  "AlphaBeta" should "pass this sanity test" in {
    // https://en.wikipedia.org/wiki/Alpha%E2%80%93beta_pruning#/media/File:AB_pruning.svg

    val tree =
      Node(Int.MinValue, Stream(
        Node(Int.MaxValue, Stream(
          Leaf(5),
          Leaf(3)
        )),
        Node(Int.MaxValue, Stream(
          Leaf(6),
          Leaf(7)
        )),
        Node(Int.MaxValue, Stream(
          Leaf(5),
          Leaf(8)
        ))
      ))

    val (bestVal, bestMove) = Minimax.alphaBeta(tree, depth = 4)

    bestVal shouldEqual 6
    bestMove shouldEqual 1
  }

}
