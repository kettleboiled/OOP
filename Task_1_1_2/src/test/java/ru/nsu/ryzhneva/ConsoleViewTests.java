package ru.nsu.ryzhneva;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ConsoleViewTests {

    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private final PrintStream originalOut = System.out;

    @BeforeEach
    void setUpStreams() {
        System.setOut(new PrintStream(outContent));
    }

    @AfterEach
    void restoreStreams() {
        System.setOut(originalOut);
    }

    @Test
    void testWelcome() {
        ConsoleView.welcome();
        assertEquals("Welcome to Blackjack!" + System.lineSeparator(), outContent.toString());
    }

    @Test
    void testRoundStart() {
        ConsoleView.roundStart(2);
        assertEquals("\nRound 2\r\nDealer dealt the cards\r\n", outContent.toString());
    }

    @Test
    void testPlayerTurn() {
        ConsoleView.playerTurn();
        assertEquals("\nYour turn\r\n-------\r\n", outContent.toString());
    }

    @Test
    void testDealerTurn() {
        ConsoleView.dealerTurn();
        assertEquals("\nDealer's turn\r\n-------\r\n", outContent.toString());
    }

    @Test
    void testShowCards_PlayerOpen() {
        List<Card> cards = List.of(new Card(0, 0), new Card(1, 9)); // Ace Spades, Ten Hearts
        ConsoleView.showCards("player", cards, false, 21);

        assertEquals("Your cards: [Ace Spades (11), Ten Hearts (10)] => 21\r\n", outContent.toString());
    }

    @Test
    void testShowCards_DealerClosed() {
        List<Card> cards = List.of(new Card(0, 0), new Card(2, 5)); // Ace Spades, Six Diamonds
        ConsoleView.showCards("dealer", cards, true, 0);

        assertEquals("Dealer's cards: [Ace Spades (11), <hidden card>]\r\n", outContent.toString());
    }

    @Test
    void testPlayerDraw() {
        Card card = new Card(3, 12); // King Clubs
        ConsoleView.playerDraw(card);

        assertEquals("You drew King Clubs (10)\r\n", outContent.toString());
    }

    @Test
    void testDealerDraw() {
        Card card = new Card(2, 1); // Two Diamonds
        ConsoleView.dealerDraw(card);

        assertEquals("Dealer drew Two Diamonds (2)\r\n", outContent.toString());
    }

    @Test
    void testFinalScore() {
        ConsoleView.finalScore(3, 5);
        assertEquals("Game over. Final score 3:5\r\n", outContent.toString());
    }

    @Test
    void testNewDeck() {
        ConsoleView.newDeck();
        assertEquals("Deck is empty — creating a new one and shuffling.\r\n", outContent.toString());
    }
}
