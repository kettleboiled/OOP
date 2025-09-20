package ru.nsu.ryzhneva;

/**
 * Класс, представляющий игральную карту.
 * Карта имеет масть (suit) и достоинство (rank).
 */
public class Card {

    /**
     * Перечисление возможных достоинств карт.
     */
    enum Rank {
        Ace, Two, Three, Four, Five, Six, Seven,
        Eight, Nine, Ten, Jack, Queen, King
    }

    /**
     * Перечисление возможных мастей карт.
     */
    enum Suit {
        Spades, Hearts, Diamonds, Clubs
    }

    /**
     * Масть карты.
     */
    Suit suit;

    /**
     * Достоинство карты.
     */
    Rank rank;

    /**
     * Конструктор создает карту по числовым идентификаторам масти и достоинства.
     *
     * @param suit числовой идентификатор масти (0..3). 0 - Пики, 1 - Черви, 2 - Бубны, 3 - Трефы.
     * @param rank числовой идентификатор достоинства (0..12). 0 - Туз, 1 - Двойка, ..., 12 - Король.
     * @throws IllegalArgumentException если передан недопустимый идентификатор масти или достоинства.
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
     * Возвращает числовое значение карты в игре.
     * Туз может иметь значение 1 или 11 в зависимости от флага.
     * Карты с числовым достоинством (2-10) возвращают свое числовое значение.
     * Карты с картинками (Валет, Дама, Король) возвращают 10.
     *
     * @param flag если true - туз считается за 1, если false - туз считается за 11.
     * @return числовое значение карты.
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
     * Выводит текстовое представление карты в формате "Достоинство Масть (значение)".
     * Значение учитывает флаг для туза.
     *
     * @param overflow если true - туз печатается как 1, если false - как 11.
     */
    public void print(boolean overflow) {
        String s = String.format("%s %s (%d)", this.rank, this.suit, this.value(overflow));
        System.out.print(s);
    }

    /**
     * Возвращает строковое представление карты в формате "Достоинство Масть".
     *
     * @return строковое представление карты.
     */
    @Override
    public String toString() {
        return String.format("%s %s", this.rank, this.suit);
    }

}
