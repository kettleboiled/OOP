package ru.nsu.ryzhneva;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.lang.reflect.Method;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.nsu.ryzhneva.cards.Card;
import ru.nsu.ryzhneva.participants.Dealer;
import ru.nsu.ryzhneva.participants.Player;

/**
 * Тесты для класса Game.
 */
class GameTests {

    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private final PrintStream originalOut = System.out;
    private Game game;

    @BeforeEach
    void setUp() {
        System.setOut(new PrintStream(outContent));
        game = new Game();
    }

    @AfterEach
    void restoreStreams() {
        System.setOut(originalOut);
    }

    @Test
    void testBlackjackBoth() {
        game.player = new Player();
        game.player.addCard(new Card(0, 0));
        game.player.addCard(new Card(1, 12));

        game.dealer = new Dealer();
        game.dealer.addCard(new Card(2, 0));
        game.dealer.addCard(new Card(3, 12));

        game.checkBlackjack();

        String output = outContent.toString();
        assertTrue(output.contains("Distribution result"));
        assertTrue(output.contains("Both got blackjack"));
        assertEquals(0, game.winPlayer);
        assertEquals(0, game.winDealer);
    }

    @Test
    void testBlackjackPlayer() {
        game.player = new Player();
        game.player.addCard(new Card(0, 0));
        game.player.addCard(new Card(1, 12));

        game.dealer = new Dealer();
        game.dealer.addCard(new Card(2, 5));
        game.dealer.addCard(new Card(3, 7));

        game.checkBlackjack();

        String output = outContent.toString();
        assertTrue(output.contains("Blackjack! You win the round!"));
        assertEquals(1, game.winPlayer);
        assertEquals(0, game.winDealer);
    }

    @Test
    void testBlackjackDealer() {
        game.player = new Player();
        game.player.addCard(new Card(0, 5));
        game.player.addCard(new Card(1, 7)); // not 21

        game.dealer = new Dealer();
        game.dealer.addCard(new Card(2, 0));
        game.dealer.addCard(new Card(3, 12)); // 21

        game.checkBlackjack();

        String output = outContent.toString();
        assertTrue(output.contains("Dealer has blackjack. You lose the round."));
        assertEquals(0, game.winPlayer);
        assertEquals(1, game.winDealer);
    }

    @Test
    void testPlayer21() {
        game.player = new Player();
        game.player.addCard(new Card(0, 0));
        game.player.addCard(new Card(1, 9));
        game.player.addCard(new Card(2, 10));

        game.checkPlayerTurn();

        String output = outContent.toString();
        assertTrue(output.contains("You have 21, you stand."));
    }

    @Test
    void testPlayerBust() {
        game.player = new Player();
        game.player.addCard(new Card(0, 10));
        game.player.addCard(new Card(1, 10));
        game.player.addCard(new Card(2, 5)); // >21

        game.checkBust();

        String output = outContent.toString();
        assertTrue(output.contains("You busted!"));
        assertEquals(0, game.winPlayer);
        assertEquals(1, game.winDealer);
    }

    @Test
    void testFinalWinnerPlayer() {
        game.compareFinal(20, 18);

        String output = outContent.toString();
        assertTrue(output.contains("You win the round!"));
        assertEquals(1, game.winPlayer);
        assertEquals(0, game.winDealer);
    }

    @Test
    void testFinalWinnerDealer() {
        game.compareFinal(17, 19);

        String output = outContent.toString();
        assertTrue(output.contains("Dealer wins the round."));
        assertEquals(0, game.winPlayer);
        assertEquals(1, game.winDealer);
    }

    @Test
    void testFinalWinnerDraw() {
        game.compareFinal(19, 19);

        String output = outContent.toString();
        assertTrue(output.contains("It's a draw."));
        assertEquals(0, game.winPlayer);
        assertEquals(0, game.winDealer);
    }

    @Test
    void testSafeDrawFromEmptyDeckCreatesNewDeck() {
        Game game = new Game();

        game.deck.cards.clear();
        assertTrue(game.deck.isEmpty());

        Card card = invokeSafeDraw(game);

        assertNotNull(card, "safeDraw должен вернуть карту даже из пустой колоды");
        assertFalse(game.deck.isEmpty(), "После safeDraw должна появиться новая колода");
        assertTrue(outContent.toString().contains("Deck is empty"),
                "При создании новой колоды должно быть сообщение");
    }

    @Test
    void testStartAndFinalScorePrinted() {
        String input = "0\nn\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));

        Game game = new Game();
        game.start();

        String out = outContent.toString();
        assertTrue(out.contains("Welcome"), "Должно выводиться приветствие");
        assertTrue(out.contains("Final score"), "В конце должен быть итоговый счёт");
    }

    private Card invokeSafeDraw(Game game) {
        try {
            Method method = Game.class.getDeclaredMethod("safeDraw");
            method.setAccessible(true);
            return (Card) method.invoke(game);
        } catch (Exception e) {
            fail("Ошибка вызова safeDraw: " + e.getMessage());
            return null;
        }
    }
}