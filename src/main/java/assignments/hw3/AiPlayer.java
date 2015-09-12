package assignments.hw3;

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
    public int findBestPlay(GameBoard currentGame) {
        // start random play code
        Random randy = new Random();
        int playChoice = 99;

        playChoice = randy.nextInt(7);

        while (!currentGame.isValidPlay(playChoice))
            playChoice = randy.nextInt(7);

        // end random play code

        return playChoice;
    }
}
