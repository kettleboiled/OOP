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

    private ByteArrayOutputStream outContent;
    private PrintStream originalOut;
    private Game game;

    @BeforeEach
    void setUp() {
        outContent = new ByteArrayOutputStream();
        originalOut = System.out;
        System.setOut(new PrintStream(outContent));


        String input = "1";
        System.setIn(new ByteArrayInputStream(input.getBytes()));
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
        String input = "1\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));  // Подменяем ввод

        game.player = new Player();
        game.player.addCard(new Card(0, 0));  // Игрок получает карты на блэкджек
        game.player.addCard(new Card(1, 12));

        game.dealer = new Dealer();
        game.dealer.addCard(new Card(2, 5));  // Дилер не имеет блэкджека
        game.dealer.addCard(new Card(3, 7));

        game.checkBlackjack();  // Проверка на блэкджек

        String output = outContent.toString();
        assertTrue(output.contains("Blackjack! You win the round!"));
        assertEquals(1, game.winPlayer);
        assertEquals(0, game.winDealer);
    }

    @Test
    void testBlackjackDealer() {
        String input = "1\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));  // Подменяем ввод

        game.player = new Player();
        game.player.addCard(new Card(0, 10));  // Пример карт для игрока
        game.player.addCard(new Card(1, 5));

        game.dealer = new Dealer();
        game.dealer.addCard(new Card(2, 0));
        game.dealer.addCard(new Card(3, 12));

        game.checkBlackjack();  // Проверка на блэкджек

        String output = outContent.toString();
        assertTrue(output.contains("Dealer has blackjack. You lose the round."));
        assertEquals(0, game.winPlayer);
        assertEquals(1, game.winDealer);
    }

    @Test
    void testPlayer21() {
        String input = "1\n"; //
        System.setIn(new ByteArrayInputStream(input.getBytes()));
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
        String input = "1\n";  // Вводим количество колод 1
        System.setIn(new ByteArrayInputStream(input.getBytes()));  // Подменяем ввод

        game.player = new Player();
        game.player.addCard(new Card(0, 10));  // Игроку карты 10 и 10
        game.player.addCard(new Card(1, 10));
        game.player.addCard(new Card(2, 5));  // Игроку перебор (сумма 25)

        game.checkBust();  // Проверка на перебор у игрока

        String output = outContent.toString();
        assertTrue(output.contains("You busted!"));  // Ожидаемое сообщение
        assertEquals(0, game.winPlayer);
        assertEquals(1, game.winDealer);
    }

    @Test
    void testFinalWinnerPlayer() {
        String input = "1\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));

        game.compareFinal(20, 18);  // Игрок выигрывает (20 против 18)

        String output = outContent.toString();
        assertTrue(output.contains("You win the round!"));
        assertEquals(1, game.winPlayer);
        assertEquals(0, game.winDealer);
    }

    @Test
    void testFinalWinnerDealer() {
        String input = "1\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));

        game.compareFinal(18, 20);

        String output = outContent.toString();
        assertTrue(output.contains("Dealer wins the round."));
        assertEquals(0, game.winPlayer);
        assertEquals(1, game.winDealer);
    }


    @Test
    void testFinalWinnerDraw() {
        String input = "1\n"; //
        System.setIn(new ByteArrayInputStream(input.getBytes()));
        game.compareFinal(19, 19);

        String output = outContent.toString();
        assertTrue(output.contains("It's a draw."));
        assertEquals(0, game.winPlayer);
        assertEquals(0, game.winDealer);
    }

    @Test
    void testSafeDrawFromEmptyDeckCreatesNewDeck() {
        String input = "1\n"; //
        System.setIn(new ByteArrayInputStream(input.getBytes()));
        Game game = new Game();

        game.deck.cards.clear();
        assertTrue(game.deck.isEmpty());

        Card card = invokeSafeDraw(game);

        assertNotNull(card, "safeDraw должен вернуть карту даже из пустой колоды");
        assertFalse(game.deck.isEmpty(), "После safeDraw должна появиться новая колода");
        assertTrue(outContent.toString().contains("Deck is empty"),
                "При создании новой колоды должно быть сообщение");
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