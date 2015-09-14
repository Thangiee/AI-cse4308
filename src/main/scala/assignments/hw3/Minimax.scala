package assignments.hw3

object Minimax {

  type BestMove = Int
  type BestVal  = Int

  def miniMax(gameTree: Node[Int], depth: Int): (BestVal, BestMove) = {

    def loop(gameTree: Tree[Int], depth: Int, maxing: Boolean): BestVal = {
      gameTree match {
        case Leaf(board)           => board
        case Node(board, subGames) =>
          if (depth == 0) return board

          if (maxing) {
            subGames.map(game => loop(game, depth - 1, maxing = false)).max
          } else {
            subGames.map(game => loop(game, depth - 1, maxing = true)).min
          }
        case Empty => if (maxing) Int.MinValue else Int.MaxValue
      }
    }

    gameTree.subTrees.map(game => loop(game, depth, maxing = false)).take(7).zipWithIndex.maxBy(_._1)
  }

  def alphaBeta(gameTree: Node[Int], depth: Int): (BestVal, BestMove) = {

    def loop(gameTree: Tree[Int], depth: Int, alpha: Int, beta: Int, index: Int, maxing: Boolean): (BestVal, BestMove) = {
      gameTree match {
        case Node(value, subTrees) =>
          if (depth <= 0) return (value, index)

          if (maxing) {
            var bestAlpha = alpha
            var bestMove = index
            for ((child, i) <- subTrees.zipWithIndex) {
              val (alphaBeta, _) = loop(child, depth-1, bestAlpha, beta, i, maxing = false)
              if (alphaBeta > bestAlpha) {
                bestAlpha = alphaBeta
                bestMove = i
              }
              if (beta <= bestAlpha) return (bestAlpha, bestMove)
            }
            (bestAlpha, bestMove)
          }
          else {
            var bestBeta = beta
            var bestMove = index
            for ((child, i) <- subTrees.zipWithIndex) {
              val (alphaBeta, _) = loop(child, depth-1, alpha, bestBeta, i, maxing = true)
              if (alphaBeta < bestBeta) {
                bestBeta = alphaBeta
                bestMove = i
              }
              if (bestBeta <= alpha) return (bestBeta, bestMove)
            }
            (bestBeta, bestMove)
          }
        case Leaf(board) => (board, index)
        case Empty => (if (maxing) Int.MinValue else Int.MaxValue, index)
      }
    }

    loop(gameTree, depth, Int.MinValue, Int.MaxValue,0, maxing = true)
  }
}
