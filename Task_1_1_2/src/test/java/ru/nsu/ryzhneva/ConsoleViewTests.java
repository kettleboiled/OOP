package ru.nsu.ryzhneva;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Тесты для класса ConsoleView.
 */
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

    // Вспомогательный метод для нормализации переносов строк
    private String normalizeLineEndings(String text) {
        return text.replace("\r\n", "\n").replace("\r", "\n");
    }

    @Test
    void testWelcome() {
        ConsoleView.welcome();
        String expected = "Welcome to Blackjack!" + System.lineSeparator();
        assertEquals(normalizeLineEndings(expected), normalizeLineEndings(outContent.toString()));
    }

    @Test
    void testRoundStart() {
        ConsoleView.roundStart(2);
        String expected = "\nRound 2\nDealer dealt the cards\n";
        assertEquals(normalizeLineEndings(expected), normalizeLineEndings(outContent.toString()));
    }

    @Test
    void testPlayerTurn() {
        ConsoleView.playerTurn();
        String expected = "\nYour turn\n-------\n";
        assertEquals(normalizeLineEndings(expected), normalizeLineEndings(outContent.toString()));
    }

    @Test
    void testDealerTurn() {
        ConsoleView.dealerTurn();
        String expected = "\nDealer's turn\n-------\n";
        assertEquals(normalizeLineEndings(expected), normalizeLineEndings(outContent.toString()));
    }

    @Test
    void testShowCardsPlayerOpen() {
        List<Card> cards = List.of(new Card(0, 0), new Card(1, 9)); // Ace Spades, Ten Hearts
        ConsoleView.showCards("player", cards, false, 21);

        String expected = "Your cards: [Ace Spades (11), Ten Hearts (10)] => 21\n";
        assertEquals(normalizeLineEndings(expected), normalizeLineEndings(outContent.toString()));
    }

    @Test
    void testShowCardsDealerClosed() {
        List<Card> cards = List.of(new Card(0, 0), new Card(2, 5)); // Ace Spades, Six Diamonds
        ConsoleView.showCards("dealer", cards, true, 0);

        String expected = "Dealer's cards: [Ace Spades (11), <hidden card>]\n";
        assertEquals(normalizeLineEndings(expected), normalizeLineEndings(outContent.toString()));
    }

    @Test
    void testPlayerDraw() {
        Card card = new Card(3, 12); // King Clubs
        ConsoleView.playerDraw(card);

        String expected = "You drew King Clubs (10)\n";
        assertEquals(normalizeLineEndings(expected), normalizeLineEndings(outContent.toString()));
    }

    @Test
    void testDealerDraw() {
        Card card = new Card(2, 1); // Two Diamonds
        ConsoleView.dealerDraw(card);

        String expected = "Dealer drew Two Diamonds (2)\n";
        assertEquals(normalizeLineEndings(expected), normalizeLineEndings(outContent.toString()));
    }

    @Test
    void testAskAction() {
        ConsoleView.askAction();
        String expected = "Enter 1 to draw a card, or 0 to stand.\n";
        assertEquals(normalizeLineEndings(expected), normalizeLineEndings(outContent.toString()));
    }

    @Test
    void testWrongAction() {
        ConsoleView.wrongAction();
        String expected = "Invalid input. Enter 1 or 0.\n";
        assertEquals(normalizeLineEndings(expected), normalizeLineEndings(outContent.toString()));
    }

    @Test
    void testPlayerBust() {
        ConsoleView.playerBust();
        String expected = "You busted!\n";
        assertEquals(normalizeLineEndings(expected), normalizeLineEndings(outContent.toString()));
    }

    @Test
    void testPlayer21() {
        ConsoleView.player21();
        String expected = "You have 21, you stand.\n";
        assertEquals(normalizeLineEndings(expected), normalizeLineEndings(outContent.toString()));
    }

    @Test
    void testDealerBust() {
        ConsoleView.dealerBust();
        String expected = "Dealer busted. You win the round!\n";
        assertEquals(normalizeLineEndings(expected), normalizeLineEndings(outContent.toString()));
    }

    @Test
    void testBlackjackBoth() {
        ConsoleView.blackjackBoth();
        String expected = "Both got blackjack — it's a draw.\n";
        assertEquals(normalizeLineEndings(expected), normalizeLineEndings(outContent.toString()));
    }

    @Test
    void testBlackjackPlayer() {
        ConsoleView.blackjackPlayer();
        String expected = "Blackjack! You win the round!\n";
        assertEquals(normalizeLineEndings(expected), normalizeLineEndings(outContent.toString()));
    }

    @Test
    void testBlackjackDealer() {
        ConsoleView.blackjackDealer();
        String expected = "Dealer has blackjack. You lose the round.\n";
        assertEquals(normalizeLineEndings(expected), normalizeLineEndings(outContent.toString()));
    }

    @Test
    void testWinPlayer() {
        ConsoleView.winPlayer();
        String expected = "You win the round!\n";
        assertEquals(normalizeLineEndings(expected), normalizeLineEndings(outContent.toString()));
    }

    @Test
    void testWinDealer() {
        ConsoleView.winDealer();
        String expected = "Dealer wins the round.\n";
        assertEquals(normalizeLineEndings(expected), normalizeLineEndings(outContent.toString()));
    }

    @Test
    void testDraw() {
        ConsoleView.draw();
        String expected = "It's a draw.\n";
        assertEquals(normalizeLineEndings(expected), normalizeLineEndings(outContent.toString()));
    }

    @Test
    void testShowScore() {
        ConsoleView.showScore(3, 5);
        String expected = "Score 3:5\n";
        assertEquals(normalizeLineEndings(expected), normalizeLineEndings(outContent.toString()));
    }

    @Test
    void testFinalScore() {
        ConsoleView.finalScore(3, 5);
        String expected = "Game over. Final score 3:5\n";
        assertEquals(normalizeLineEndings(expected), normalizeLineEndings(outContent.toString()));
    }

    @Test
    void testAskAgain() {
        ConsoleView.askAgain();
        String expected = "Do you want to play again? (y/n): ";
        assertEquals(normalizeLineEndings(expected), normalizeLineEndings(outContent.toString()));
    }

    @Test
    void testNewDeck() {
        ConsoleView.newDeck();
        String expected = "Deck is empty — creating a new one and shuffling.\n";
        assertEquals(normalizeLineEndings(expected), normalizeLineEndings(outContent.toString()));
    }
}