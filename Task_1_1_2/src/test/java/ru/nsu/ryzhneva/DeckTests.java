package ru.nsu.ryzhneva;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Тесты для класса Deck.
 */
class DeckTests {

    @Test
    void testDeck() {
        Deck deck = new Deck();
        assertEquals(52, deck.cards.size());
    }

    @Test
    void testDrawCard() {
        Deck d = new Deck();
        int size = d.size();
        Card c = d.drawCard();
        assertNotNull(c);
        assertEquals(size - 1, d.size());
    }

    @Test
    void emptyDeckTest() {
        Deck d = new Deck();
        for (int i = 0; i < 52; i++) {
            d.drawCard();
        }
        assertTrue(d.isEmpty());
        assertNull(d.drawCard());
    }

    @Test
    void testShuffleChangesOrder() {
        Deck d = new Deck();
        String before = d.cards.toString();
        d.shuffle();
        String after = d.cards.toString();
        assertEquals(52, d.size());
        assertNotEquals(before, after);
    }

    @Test
    void testIsEmpty() {
        Deck deck = new Deck();
        assertFalse(deck.isEmpty());

        for (int i = 0; i < 52; i++) {
            deck.drawCard();
        }

        assertTrue(deck.isEmpty());
    }

    @Test
    void testSize() {
        Deck deck = new Deck();
        assertEquals(52, deck.size());

        deck.drawCard();
        assertEquals(51, deck.size());
    }
}