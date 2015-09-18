package assignments.hw3

object Ai {

  /**
   * Find the best move for the game board with a given max depth
   *
   * @param gameBoard the game board of the current game
   * @param depthLevel max search depth
   * @return the best move
   */
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

  /**
   * Create a search tree from a game board
   *
   * @param gameBoard
   * @param computerSymbol
   * @return
   */
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
