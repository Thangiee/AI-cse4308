package assignments.hw3;

import scala.Int;

import java.util.Random;

/**
 * This is the AiPlayer class.  It simulates a minimax player for the max
 * connect four game.
 * The constructor essentially does nothing.
 *
 * @author james spargo
 */

public class AiPlayer {
    /**
     * The constructor essentially does nothing except instantiate an
     * AiPlayer object.
     */
    public AiPlayer() {
        // nothing to do here
    }

    /**
     * This method plays a piece randomly on the board
     *
     * @param currentGame The GameBoard object that is currently being used to
     *                    play the game.
     * @return an integer indicating which column the AiPlayer would like
     * to play in.
     */
    public int findBestPlay(GameBoard currentGame, int depthLevel) {
        return Ai.findBestPlay(currentGame, depthLevel);
    }
}
