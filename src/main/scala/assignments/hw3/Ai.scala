package assignments.hw3

object Ai {

  def findBestPlay(gameBoard: GameBoard, depthLevel: Int): Int = {
    println(s"best play for ${gameBoard.getCurrentTurn}")
    genGameTree(gameBoard) match {
      case n: Node[Int] =>
        val (_, bestMove) = Minimax.alphaBeta(n, depthLevel)
        bestMove
      case Leaf(_) =>
        throw new IllegalStateException("Can't find next move on a full board")
      case Empty =>
        throw new IllegalArgumentException("Invalid game board")
    }
  }

  private def genGameTree(gameBoard: GameBoard): Tree[Int] = {
    if (gameBoard.getPieceCount == 42) return Leaf(eval(gameBoard))

    Node(
      value = eval(gameBoard),
      subTrees = (0 until 7).toStream.map { i =>
        val newBoard = new GameBoard(gameBoard.getGameBoard)
        if (newBoard.playPiece(i)) genGameTree(newBoard)
        else Empty
      }
    )
  }

  private def eval(gameBoard: GameBoard): Int = {
    if (gameBoard.getCurrentTurn == 1)
      gameBoard.getScore(1) - gameBoard.getScore(2)
    else
      gameBoard.getScore(2) - gameBoard.getScore(1)
  }
}
