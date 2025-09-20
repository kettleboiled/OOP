package ru.nsu.ryzhneva;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class Deck {
    List<Card> cards;
    private final Random rand = new Random();

    public Deck() {
        cards = new ArrayList<>();

        for (int i = 0; i < 52; i++) {
            Card temp = new Card(i / 13, i % 13);
            cards.add(temp);
        }
    }

    /**
     * Fisher–Yates shuffle.
     */
    public void shuffle() {
        for (int i = cards.size() - 1; i > 0; i--) {
            int j = rand.nextInt(i + 1); // 0..i
            Collections.swap(cards, i, j);
        }
    }

    /**
     * Draw (remove and return) top card. If empty -> return null.
     * We treat end of list as "top" (consistent with previous code).
     */
    public Card drawCard() {
        if (cards.isEmpty()) return null;
        return cards.remove(cards.size() - 1);
    }

    public boolean isEmpty() {
        return cards.isEmpty();
    }

    public int size() {
        return cards.size();
    }
}
