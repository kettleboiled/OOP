package ru.nsu.ryzhneva;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.nsu.ryzhneva.cards.Card;
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

    private String normalizeLineEndings(String text) {
        return text.replace("\r\n", "\n").replace("\r", "\n");
    }

    @Test
    void testWelcome() {
        ConsoleView.welcome();
        String expected = "Welcome to Blackjack!" + System.lineSeparator();
        assertEquals(normalizeLineEndings(expected),
                normalizeLineEndings(outContent.toString()));
    }

    @Test
    void testRoundStart() {
        ConsoleView.roundStart(2);
        String expected = "\nRound 2\nDealer dealt the cards\n";
        assertEquals(normalizeLineEndings(expected),
                normalizeLineEndings(outContent.toString()));
    }

    @Test
    void testPlayerTurn() {
        ConsoleView.playerTurn();
        String expected = "\nYour turn\n-------\n";
        assertEquals(normalizeLineEndings(expected),
                normalizeLineEndings(outContent.toString()));
    }

    @Test
    void testDealerTurn() {
        ConsoleView.dealerTurn();
        String expected = "\nDealer's turn\n-------\n";
        assertEquals(normalizeLineEndings(expected),
                normalizeLineEndings(outContent.toString()));
    }

    @Test
    void testShowCardsPlayerOpen() {
        List<Card> cards = List.of(new Card(0, 0), new Card(1, 9));
        ConsoleView.showCards("player", cards, false, 21);
        String expected = "Your cards: [Ace Spades (11), Ten Hearts (10)] => 21\n";
        assertEquals(normalizeLineEndings(expected),
                normalizeLineEndings(outContent.toString()));
    }

    @Test
    void testShowCardsDealerClosed() {
        List<Card> cards = List.of(new Card(0, 0), new Card(2, 5));
        ConsoleView.showCards("dealer", cards, true, 0);
        String expected = "Dealer's cards: [Ace Spades (11), <hidden card>]\n";
        assertEquals(normalizeLineEndings(expected),
                normalizeLineEndings(outContent.toString()));
    }

    @Test
    void testPlayerDraw() {
        Card card = new Card(3, 12);
        ConsoleView.playerDraw(card);
        String expected = "You drew King Clubs (10)\n";
        assertEquals(normalizeLineEndings(expected),
                normalizeLineEndings(outContent.toString()));
    }

    @Test
    void testDealerDraw() {
        Card card = new Card(2, 1);
        ConsoleView.dealerDraw(card);
        String expected = "Dealer drew Two Diamonds (2)\n";
        assertEquals(normalizeLineEndings(expected),
                normalizeLineEndings(outContent.toString()));
    }

    @Test
    void testAskAction() {
        ConsoleView.askAction();
        String expected = "Enter 1 to draw a card, or 0 to stand.\n";
        assertEquals(normalizeLineEndings(expected),
                normalizeLineEndings(outContent.toString()));
    }

    @Test
    void testWrongAction() {
        ConsoleView.wrongAction();
        String expected = "Invalid input. Enter 1 or 0.\n";
        assertEquals(normalizeLineEndings(expected),
                normalizeLineEndings(outContent.toString()));
    }

    @Test
    void testPlayerBust() {
        ConsoleView.playerBust();
        String expected = "You busted!\n";
        assertEquals(normalizeLineEndings(expected),
                normalizeLineEndings(outContent.toString()));
    }

    @Test
    void testPlayer21() {
        ConsoleView.player21();
        String expected = "You have 21, you stand.\n";
        assertEquals(normalizeLineEndings(expected),
                normalizeLineEndings(outContent.toString()));
    }

    @Test
    void testDealerBust() {
        ConsoleView.dealerBust();
    }
}