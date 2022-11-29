package org.yogesh;


import static org.yogesh.BowlingGameBusinessLogic.KNOCKED_PINS_LESS_THAN_ZERO;
import static org.yogesh.BowlingGameBusinessLogic.KNOCKED_PINS_GREATER_THAN_TEN;
import static org.yogesh.BowlingGameBusinessLogic.GAME_ALREADY_OVER;

/**
 * An implementation of the {@link BowlingGameInterface}.
 * First 9 frames are supposed to have 2 rolls at a maximum.
 * 10th frame can conditionally have a 3rd roll.
 * So a maximum of {@link #MAX_ROLLS} rolls can be in the game.
 * This implementation captures the information about each roll in {@link #rollsInfo}.
 * The implementation does not use any specific data structure to indicate the concept of Frame in the Bowling game,
 * but {@link #rollsInfo} can be traversed in a manner it can deduce the concept of Frame where first 9 frames can
 * have a maximum of 2 rolls and 10th frame can conditionally have an additional roll.
 */
public class BowlingGame implements BowlingGameInterface{

    /**
     * Data about each roll.
     */
    private final RollInfo [] rollsInfo;
    /**
     * Pre create room for maximum number of rolls in a single player game.
     */
    private final int MAX_ROLLS = 21;

    /**
     * Indicator where the data should be stored for the next roll in the game.
     */
    private int nextRollSlot = 0;


    private boolean isNextRollTypeExpectedAsData = false;

    private boolean gameOver = false;

    private static final int TEN = 10;

    /**
     * Use static method {@link #bowlingGameInstance()} to get an implementing instance of the {@link BowlingGameInterface}
     */
    private BowlingGame() {
        rollsInfo = new RollInfo [MAX_ROLLS];
        for(int i = 0; i < rollsInfo.length; i++) {
            rollsInfo[i] = new RollInfo(0);
        }
    }

    /**
     * static method to construct an implementing instance of the interface {@link BowlingGameInterface}
     * @return An instance of {@link BowlingGame}
     */
    public static BowlingGameInterface bowlingGameInstance() {

        return new BowlingGame();
    }

    /**
     * This method should be called to play a roll. If an exception is thrown by the method, then it should not change the
     * score in the game.
     *
     * @param noOfPinsKnockedDown Number of pins knocked down in this roll
     * @throws IllegalArgumentException When @param noOfPinsKnockedDown is greater than 10 or less than 0.
     * @throws IllegalStateException    When the game is already over.
     */
    @Override
    public void roll(final int noOfPinsKnockedDown) throws IllegalArgumentException, IllegalStateException {

        //Sanity check
        if(noOfPinsKnockedDown < 0) {
            throw new IllegalArgumentException(KNOCKED_PINS_LESS_THAN_ZERO);
        }
        if(noOfPinsKnockedDown > TEN) {
            throw new IllegalArgumentException(KNOCKED_PINS_GREATER_THAN_TEN);
        }
        if(nextRollSlot == MAX_ROLLS || gameOver) {
            throw new IllegalStateException(GAME_ALREADY_OVER);
        }

        if(!isNextRollTypeExpectedAsData) {
            //Strike
            if(noOfPinsKnockedDown == TEN) {
                rollsInfo[nextRollSlot].pinsStriked = noOfPinsKnockedDown;
                rollsInfo[nextRollSlot++].rollType = RollType.STRIKE;
                //Handling first 9 frames
                if(nextRollSlot < MAX_ROLLS && nextRollSlot < 18) {
                    rollsInfo[nextRollSlot].pinsStriked = 0;
                    rollsInfo[nextRollSlot++].rollType = RollType.IGNORE;

                } else { //handling 10th frame
                    //nothing additional to do here.
                }
                isNextRollTypeExpectedAsData = false;
            } else {
                rollsInfo[nextRollSlot].pinsStriked = noOfPinsKnockedDown;
                rollsInfo[nextRollSlot++].rollType = RollType.DATA;
                isNextRollTypeExpectedAsData = true;
            }
        } else {
            if(rollsInfo[nextRollSlot - 1].pinsStriked + noOfPinsKnockedDown > TEN) {
                throw new IllegalArgumentException("Illegal input for this roll: " + noOfPinsKnockedDown + ". Previous roll in this frame " +
                        "already knocked down " + rollsInfo[nextRollSlot - 1].pinsStriked + " pins. \n" + KNOCKED_PINS_GREATER_THAN_TEN);
            } else {
                rollsInfo[nextRollSlot].pinsStriked = noOfPinsKnockedDown;
                rollsInfo[nextRollSlot++].rollType = RollType.DATA;
                if(nextRollSlot == 20) {
                    //additional handling for 10th frame.
                    if((rollsInfo[18].rollType == RollType.STRIKE) || (rollsInfo[18].pinsStriked + rollsInfo[19].pinsStriked == TEN) ){
                        isNextRollTypeExpectedAsData = false;
                    } else {
                        gameOver = true;
                    }
                }
                isNextRollTypeExpectedAsData = false;
            }
        }

    }

    /**
     * @return The score so far in the Bowling game.
     */
    @Override
    public int score() {
        int retVal = 0;
        for(int i=0; i<nextRollSlot; i++) {
            retVal += rollsInfo[i].pinsStriked;
            if(rollsInfo[i].rollType == RollType.STRIKE) {
                if(i < 18) {
                    retVal += getNextTwoRollScores(i);
                }
            } else if(rollsInfo[i].rollType == RollType.DATA) {
                if(i < 18) {
                    if(i+1 < nextRollSlot) {
                        int nextData = rollsInfo[i+1].pinsStriked;
                        retVal += nextData;
                        if(nextData + rollsInfo[i].pinsStriked == TEN) {//Spare
                            retVal += getNextRollScore(i+1);
                        }
                        i++;
                    }
                } else {
                    if(i+1 < nextRollSlot) {
                        int nextData = rollsInfo[i+1].pinsStriked;
                        retVal += nextData;
                        i++;
                    }
                }
            }
        }
        return retVal;
    }

    private int getNextRollScore(int startOffsetExclusive) {
        int retVal = 0;
        for(int i = startOffsetExclusive+1; i < nextRollSlot; i++) {
            if(rollsInfo[i].rollType != RollType.IGNORE) {
                retVal += rollsInfo[i].pinsStriked;
                break;
            }
        }
        return retVal;
    }

    private int getNextTwoRollScores(int startOffsetExclusive) {
        int retVal = 0;
        boolean isFirst = true;
        for(int i = startOffsetExclusive+1; i < nextRollSlot; i++) {
            if(rollsInfo[i].rollType != RollType.IGNORE) {
                retVal += rollsInfo[i].pinsStriked;
                if(isFirst) {
                  isFirst = false;
                  continue;
                }
                break;
            }
        }
        return retVal;
    }

    private enum RollType {
        //Indicates a Strike
        STRIKE,
        //Indicator for padding in the Frame when there is a Strike.
        IGNORE,
        //Indicator for a NON strike situation.
        DATA
    }

    private static class RollInfo {
        private int pinsStriked;
        private RollType rollType;

        RollInfo(int pinsStriked) {
            this(pinsStriked, RollType.IGNORE);
        }

        RollInfo(int pinsStriked, RollType rollType) {
            this.pinsStriked = pinsStriked;
            this.rollType = rollType;
        }
    }
}
