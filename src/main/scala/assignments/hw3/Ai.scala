package assignments.hw3

object Ai {

  def findBestPlay(gameBoard: GameBoard, depthLevel: Int): Int = {
    
    genGameTree(gameBoard, gameBoard.getCurrentTurn) match {
      case n: Node[Int] =>
        val (_, bestMove) = Minimax.alphaBeta(n, depthLevel)
        bestMove
      case Leaf(_) =>
        throw new IllegalStateException("Can't find next move on a full board")
      case Empty =>
        throw new IllegalArgumentException("Invalid game board")
    }
  }

  def genGameTree(gameBoard: GameBoard, computerSymbol: Int): Tree[Int] = {
    if (gameBoard.getPieceCount == 42) return Leaf(eval(gameBoard, computerSymbol))

    Node(
      value = eval(gameBoard, computerSymbol),
      subTrees = (0 until 7).toStream.map { i =>
        val newBoard = new GameBoard(gameBoard.getGameBoard)
        if (newBoard.playPiece(i)) genGameTree(newBoard, computerSymbol)
        else Empty
      }
    )
  }

  private def eval(gameBoard: GameBoard, computerSymbol: Int): Int = {
    if (computerSymbol == 1)
      gameBoard.getScore(1) - gameBoard.getScore(2)
    else
      gameBoard.getScore(2) - gameBoard.getScore(1)
  }
}
