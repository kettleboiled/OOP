package ru.nsu.ryzhneva;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.nsu.ryzhneva.cards.Card;
import ru.nsu.ryzhneva.participants.Player;

/**
 * Тесты для класса Player.
 */
class PlayerTests {

    private Player player;

    @BeforeEach
    void setUp() {
        player = new Player();
    }

    @Test
    void testNewPlayerHandIsEmpty() {
        assertEquals(0, player.getHand().size(),
                "При создании игрока рука должна быть пустой.");
    }

    @Test
    void testAddCard() {
        Card card = new Card(0, 0); // Ace Spades
        player.addCard(card);
        assertEquals(1, player.getHand().size());
        assertTrue(player.getHand().contains(card));
    }

    @Test
    void testGetValueWithoutAce() {
        player.addCard(new Card(0, 9)); // Ten
        player.addCard(new Card(1, 8)); // Nine
        assertEquals(19, player.getValue(),
                "Значение должно корректно суммироваться без туза.");
    }

    @Test
    void testGetValueWithAceAs11() {
        player.addCard(new Card(0, 0)); // Ace
        player.addCard(new Card(1, 8)); // Nine
        assertEquals(20, player.getValue(),
                "Туз должен считаться за 11, если не перебор.");
    }

    @Test
    void testGetValueWithAceAs1() {
        player.addCard(new Card(0, 0)); // Ace
        player.addCard(new Card(1, 12)); // King
        player.addCard(new Card(2, 11)); // Queen
        assertEquals(21, player.getValue(),
                "Туз должен считаться за 1, если иначе перебор.");
    }

    @Test
    void testGetValueBustWithoutAce() {
        player.addCard(new Card(0, 12)); // King = 10
        player.addCard(new Card(1, 11)); // Queen = 10
        player.addCard(new Card(2, 4));  // Five = 5
        assertEquals(25, player.getValue(),
                "Если нет туза, перебор должен быть честным.");
    }

    @Test
    void testPrintStringEmptyHand() {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        System.setOut(new PrintStream(output));

        player.printString(true, false);

        assertTrue(output.toString().trim().contains("No cards in hand"),
                "Должно быть сообщение об отсутствии карт.");
    }

    @Test
    void testPrintStringWithCardsDoesNotThrow() {
        player.addCard(new Card(0, 0)); // Ace
        player.addCard(new Card(1, 9)); // Ten
        assertDoesNotThrow(() -> player.printString(true, false),
                "Вывод с картами не должен кидать исключения.");
    }
}
