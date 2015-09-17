package assignments.hw3

import assignments.BaseSpec
import org.scalacheck.Gen

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

  "AlphaBeta and minimax" must "yield the same result regardless of alphaBeta pruning" in {

    val genTree =
      for {
        listSize <- Gen.choose(1, 41)
        moves <- Gen.listOfN(listSize, Gen.choose(0, 6))
      } yield {
        val gb = new GameBoard(Array.fill(6, 7)(0))
        moves.foreach(move => gb.playPiece(move))
        Ai.genGameTree(gb, gb.getCurrentTurn)
      }

    val searchDepth = Gen.choose(0, 4)

    forAll(genTree, searchDepth) { case (n: Node[Int], dept) =>
      Minimax.alphaBeta(n.copy(), dept) shouldEqual Minimax.miniMax(n.copy(), dept)
    }
  }

}
