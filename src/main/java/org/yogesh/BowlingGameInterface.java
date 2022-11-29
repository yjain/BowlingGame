package org.yogesh;

public interface BowlingGameInterface {

    /**
     * This method should be called to play a roll. If an exception is thrown by the method, then it should not change the 
     * score in the game.
     * @param noOfPinsKnockedDown Number of pins knocked down in this roll
     * @throws IllegalArgumentException When @param noOfPinsKnockedDown is greater than 10 or less than 0.
     * @throws IllegalStateException When the game is already over.
     */   
    void roll(int noOfPinsKnockedDown) throws IllegalArgumentException, IllegalStateException;

    /**
     * 
     * @return The score so far in the Bowling game.
     */
    int score();
    
}
