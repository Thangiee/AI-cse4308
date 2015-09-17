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

    def loop(gameTree: Tree[Int], depth: Int, alpha: Int, beta: Int, maxing: Boolean): BestVal = {
      gameTree match {
        case Node(value, subTrees) =>
          if (depth == 0) return value

          if (maxing) {
            var a = alpha
            for (child <- subTrees) {
              a = a max loop(child, depth-1, a, beta, maxing = false)
              if (beta <= a) return a
            }
            a
          }
          else {
            var b = beta
            for (child <- subTrees) {
              b = b min loop(child, depth-1, alpha, b, maxing = true)
              if (b <= alpha) return b
            }
            b
          }
        case Leaf(board) => board
        case Empty => if (maxing) Int.MaxValue else Int.MinValue
      }
    }

    var alphas: List[Int] = Nil
    var alpha = Int.MinValue

    for(child <- gameTree.subTrees) {
      val a = loop(child, depth-1, alpha, Int.MaxValue, maxing = false)
      if (alpha < a) {
        alpha = a
      }
      alphas = alphas :+ a
    }

    alphas.zipWithIndex.maxBy { case (bestVal, bestMove) => bestVal }
  }
}
