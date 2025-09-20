package ru.nsu.ryzhneva;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

/**
 * Формирует колоду из 52 карт и предоставляет методы для работы с этой колодой.
 */
public class Deck {
    List<Card> cards;
    private final Random rand = new Random();

    /**
     * Конструктор создает стандартную колоду из 52 карт.
     * Карты создаются в упорядоченном виде по мастям и достоинствам.
     */
    public Deck() {
        cards = new ArrayList<>();

        for (int i = 0; i < 52; i++) {
            Card temp = new Card(i / 13, i % 13);
            cards.add(temp);
        }
    }

    /**
     * Перемешивает колоду используя алгоритм Fisher-Yates shuffle.
     * Алгоритм обеспечивает равномерное случайное распределение карт.
     */
    public void shuffle() {
        for (int i = cards.size() - 1; i > 0; i--) {
            int j = rand.nextInt(i + 1); // 0..i
            Collections.swap(cards, i, j);
        }
    }

    /**
     * Берет верхнюю карту из колоды.
     * Карта удаляется из колоды после взятия.
     *
     * @return верхняя карта колоды или null если колода пуста
     */
    public Card drawCard() {
        if (cards.isEmpty()) return null;
        return cards.remove(cards.size() - 1);
    }

    /**
     * Проверяет, пуста ли колода.
     *
     * @return true если колода пуста, иначе false
     */
    public boolean isEmpty() {
        return cards.isEmpty();
    }

    /**
     * Возвращает количество оставшихся карт в колоде.
     *
     * @return количество карт в колоде
     */
    public int size() {
        return cards.size();
    }
}
