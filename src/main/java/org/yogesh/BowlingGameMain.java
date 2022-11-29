package org.yogesh;

import java.util.Scanner;

public class BowlingGameMain {

    public static void main(String [] args) {
        BowlingGameInterface bowling = BowlingGame.bowlingGameInstance();
        Scanner scanner = new Scanner(System.in);
        int roll = 1;
        String welcomeMessage = "Single player bowling game offering by Yogesh.\n"
                + "To play the game, you should enter a number in the range 0-10 both numbers inclusive.\n"
                + "Computer will play the number you input as number of pins hit down in that roll.\n"
                + "Computer will then print the total score so far in the game.\n";

        System.out.println(welcomeMessage);
        while(true) {
            int pinsKnockedDown = scanner.nextInt();

            System.out.println("Playing roll " + roll++ + " with " + pinsKnockedDown + " pins down.");
            bowling.roll(pinsKnockedDown);
            System.out.println("Game score : " + bowling.score());
            System.out.println();
        }
    }
}
