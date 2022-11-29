package org.yogesh;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.yogesh.BowlingGame.bowlingGameInstance;
import static org.yogesh.BowlingGameBusinessLogic.KNOCKED_PINS_GREATER_THAN_TEN;
import static org.yogesh.BowlingGameBusinessLogic.KNOCKED_PINS_LESS_THAN_ZERO;

public class BowlingGameTest {

    @Test
    public void testWhenRollInputIsLessThanZeroThenItThrowsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () ->roll(bowlingGameInstance(), -1),
                () -> KNOCKED_PINS_LESS_THAN_ZERO);
    }

    @Test
    public void testWhenRollInputIsGreaterThanTenThenItThrowsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () ->roll(bowlingGameInstance(),11),
                () -> KNOCKED_PINS_GREATER_THAN_TEN);
    }

    @Test
    public void testRollingThrowsIllegalArgumentExceptionWhenGameOver(){

        int [] rollsWithNoStrikeAndNoSpareInTenthFrame = {
                0,0,
                0,0,
                0,0,
                0,0,
                0,0,
                0,0,
                0,0,
                0,0,
                0,0,
                0,0,
                0 //game end indicator

        };
        assertThrows(IllegalStateException.class, () -> roll(bowlingGameInstance(), rollsWithNoStrikeAndNoSpareInTenthFrame));

        int [] rollsWithASpareInTenthFrame = {
                0,0,
                0,0,
                0,0,
                0,0,
                0,0,
                0,0,
                0,0,
                0,0,
                0,0,
                0,10,0, //spare in 10th frame
                0 //game end indicator
        };
        assertThrows(IllegalStateException.class, () -> roll(bowlingGameInstance(), rollsWithASpareInTenthFrame));

        int [] rollsWithAStrikeInTenthFrame = {
                0,0,
                0,0,
                0,0,
                0,0,
                0,0,
                0,0,
                0,0,
                0,0,
                0,0,
                10,0,0,//strike in 10th frame
                0 //game end indicator
        };
        assertThrows(IllegalStateException.class, () -> roll(bowlingGameInstance(), rollsWithAStrikeInTenthFrame));

        int [] rollsWithJustOnePinDownInTenthFrame = {
                0,0,
                0,0,
                0,0,
                0,0,
                0,0,
                0,0,
                0,0,
                0,0,
                0,0,
                1,0,
                0 //game end indicator
        };
        assertThrows(IllegalStateException.class, () -> roll(bowlingGameInstance(), rollsWithJustOnePinDownInTenthFrame));
    }

    @Test
    public void testWhenNoRollThenScoreIsZero() {
        assertEquals(0, bowlingGameInstance().score());
    }

    @Test
    public void testMaximumScoreIs300() {
        int [] rollsWithAllStrikes = {//12 strikes
                10,
                10,
                10,
                10,
                10,
                10,
                10,
                10,
                10,
                10,10,10
        };
        BowlingGameInterface bowling = bowlingGameInstance();
        roll(bowling, rollsWithAllStrikes);
        assertEquals(300, bowling.score());
    }

    @Test
    public void testPlayingAFullGame1() {
        int [] rolls = {
                10, //Strike
                9,1,//Spare
                5,5,//Spare
                7,2,
                10, //Strike
                10, //Strike
                10, //Strike
                9,0,
                8,2,//Spare
                9,1,10//spare and strike in 10th frame
        };
        BowlingGameInterface bowling = bowlingGameInstance();
        roll(bowling, rolls);
        assertEquals(187, bowling.score());
    }

    @Test
    public void testPlayingAFullGame2() {
        int [] rolls = {
                10, //Strike
                9,1,//Spare
                5,5,//Spare
                7,2,
                10, //Strike
                10, //Strike
                10, //Strike
                9,0,
                8,2,//Spare
                9,1,1//spare in 10th frame
        };
        BowlingGameInterface bowling = bowlingGameInstance();
        roll(bowling, rolls);
        assertEquals(178, bowling.score());
    }

    private static void roll(BowlingGameInterface bowling, int ...noOfPinsKnockedDown ) {
        for(int pinsDown : noOfPinsKnockedDown) {
            bowling.roll(pinsDown);
        }
    }
}
