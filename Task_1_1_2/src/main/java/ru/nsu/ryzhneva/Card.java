package ru.nsu.ryzhneva;

public class Card {

    enum Rank {
        Ace, Two, Three, Four, Five, Six, Seven,
        Eight, Nine, Ten, Jack, Queen, King
    }

    enum Suit {
        Spades, Hearts, Diamonds, Clubs
    }

    Suit suit;
    Rank rank;

    /**
     *  * Suit: 0..3 (Пики, Черви, Бубны, трефы)
     *  * Rank: 0..12 (туз .. Король)
     */
    public Card(int suit, int rank) throws IllegalArgumentException {
        if (suit < 0 || suit >= Suit.values().length) {
            throw new IllegalArgumentException("Wrong suit: " + suit);
        }
        if (rank < 0 || rank >= Rank.values().length) {
            throw new IllegalArgumentException("Wrong dignity: " + rank);
        }
        this.suit = Suit.values()[suit];
        this.rank = Rank.values()[rank];
    }

    /**
     * Возвращает значение карты. Если флаг == true, то туз принимается за 1; в противном случае туз принимается за 11.
     */
    public int value(boolean flag) {
        int r = this.rank.ordinal() + 1;

        if (r == 1) { // Ace
            return flag ? 1 : 11;
        } else if (r >= 2 && r <= 10) {
            return r;
        } else {
            return 10;
        }
    }

    /**
     * Print representation like: "Ace Spades (11)".
     * overflow == true means Ace printed as 1 (value(overflow) used).
     */
    public void print(boolean overflow) {
        System.out.print(this.rank + " " + this.suit + " (" + this.value(overflow) + ")");
    }

    @Override
    public String toString() {
        return this.rank + " " + this.suit;
    }
}
