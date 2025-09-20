package ru.nsu.ryzhneva;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

/**
 * Тесты для класса Card.
 */
public class CardTests {

    @Test
    void testValue() {
        Card ace = new Card(0, 0);
        assertEquals(11, ace.value(false));
        assertEquals(1, ace.value(true));

        Card five = new Card(0, 4);
        assertEquals(5, five.value(false));

        Card king = new Card(0, 12);
        assertEquals(10, king.value(false));
    }

    @Test
    void testCard() {
        Card c = new Card(0, 0);
        assertEquals(Card.Suit.Spades, c.suit);
        assertEquals(Card.Rank.Ace, c.rank);
    }

    @Test
    void testToString() {
        Card c = new Card(2, 10); // Jack Diamonds
        assertTrue(c.toString().contains("Jack"));
        assertTrue(c.toString().contains("Diamonds"));
    }

    @Test
    void testPrint() {
        Card card = new Card(1, 5);
        assertDoesNotThrow(() -> card.print(false));
    }

    // Тесты на неправильные индексы (конструктор выбрасывает IllegalArgumentException)
    @Test
    void testInvalidSuitNegative() {
        assertThrows(IllegalArgumentException.class, () -> new Card(-1, 0));
    }

    @Test
    void testInvalidSuitTooBig() {
        assertThrows(IllegalArgumentException.class, () -> new Card(4, 0));
    }

    @Test
    void testInvalidRankNegative() {
        assertThrows(IllegalArgumentException.class, () -> new Card(0, -1));
    }

    @Test
    void testInvalidRankTooBig() {
        assertThrows(IllegalArgumentException.class, () -> new Card(0, 13));
    }
}
