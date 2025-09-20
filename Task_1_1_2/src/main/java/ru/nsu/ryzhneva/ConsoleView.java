package ru.nsu.ryzhneva;

import java.util.List;

public class ConsoleView{

    public static void welcome() {
        System.out.println("Welcome to Blackjack!");
    }

    public static void roundStart(int round) {
        System.out.println("\nRound " + round);
        System.out.println("Dealer dealt the cards");
    }


    public static void showCards(String owner, List<Card> cards, boolean closed, int value) {
        if (owner.equals("player")) {
            System.out.print("Your cards: [");
        } else {
            System.out.print("Dealer's cards: [");
        }

        if (closed && !cards.isEmpty()) {
            cards.get(0).print(false);
            System.out.print(", <hidden card>");
        } else {
            boolean overflow = value > 21;
            for (int i = 0; i < cards.size(); i++) {
                cards.get(i).print(overflow);
                if (i < cards.size() - 1) System.out.print(", ");
            }
        }

        System.out.print("]");
        if (!closed) {
            System.out.println(" => " + value);
        } else {
            System.out.println();
        }
    }

    public static void playerTurn() {
        System.out.println("\nYour turn");
        System.out.println("-------");
    }

    public static void dealerTurn() {
        System.out.println("\nDealer's turn");
        System.out.println("-------");
    }

    public static void askAction() {
        System.out.println("Enter 1 to draw a card, or 0 to stand.");
    }

    public static void wrongAction() {
        System.out.println("Invalid input. Enter 1 or 0.");
    }

    public static void playerDraw(Card card) {
        System.out.print("You drew ");
        card.print(false);
        System.out.println();
    }

    public static void dealerDraw(Card card) {
        System.out.print("Dealer drew ");
        card.print(false);
        System.out.println();
    }

    public static void playerBust() {
        System.out.println("You busted!");
    }

    public static void player21() {
        System.out.println("You have 21, you stand.");
    }
    public static void dealerBust() {
        System.out.println("Dealer busted. You win the round!");
    }

    public static void blackjackBoth() {
        System.out.println("Both got blackjack — it's a draw.");
    }

    public static void blackjackPlayer() {
        System.out.println("Blackjack! You win the round!");
    }

    public static void blackjackDealer() {
        System.out.println("Dealer has blackjack. You lose the round.");
    }

    public static void winPlayer() {
        System.out.println("You win the round!");
    }

    public static void winDealer() {
        System.out.println("Dealer wins the round.");
    }

    public static void draw() {
        System.out.println("It's a draw.");
    }

    public static void showScore(int player, int dealer) {
        System.out.println("Score " + player + ":" + dealer);
    }

    public static void finalScore(int player, int dealer) {
        System.out.println("Game over. Final score " + player + ":" + dealer);
    }

    public static void askAgain() {
        System.out.print("Do you want to play again? (y/n): ");
    }

    public static void newDeck() {
        System.out.println("Deck is empty — creating a new one and shuffling.");
    }
}
