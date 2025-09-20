package ru.nsu.ryzhneva;

import java.util.ArrayList;
import java.util.List;


/**
 * Класс, представляющий игрока в игре Блэкджек.
 * Содержит коллекцию карт на руке и методы для работы с ними.
 */

public class Player {
    /** Список карт на руке игрока */
    List<Card> hand;

    /**
     * Конструктор создает нового игрока с пустой рукой.
     */
    public Player() {
        hand = new ArrayList<>();
    }

    /**
     * Добавляет карту в руку игрока.
     *
     * @param card карта для добавления
     */
    public void addCard(Card card) {
        hand.add(card);
    }

    /**
     * Вычисляет текущее значение карт на руке игрока.
     * Туз считается как 11, если это не приводит к перебору (больше 21),
     * в противном случае туз считается как 1.
     *
     * @return суммарное значение карт на руке
     */
    public int getValue() {
        int sum = 0;

        for (Card card : hand) {
            sum += card.value(false);
        }

        if (sum > 21) {
            sum = 0;
            for (Card card : hand) {
                sum += card.value(true);
            }
        }
        return sum;
    }

    /**
     * Вывод карт игрока или дилера через ConsoleView
     * @param participant true — игрок, false — дилер
     * @param closedCard  true — закрытая карта дилера
     */

    public void printString(boolean participant, boolean closedCard) {
        if (hand.isEmpty()) {
            System.out.println("No cards in hand");
        } else {
            ConsoleView.showCards(
                    participant ? "player" : "dealer",
                    hand,
                    closedCard,
                    this.getValue()
            );
        }
    }

    /**
     * Возвращает список карт на руке игрока.
     *
     * @return список карт на руке
     */
    public List<Card> getHand() {
        return hand;
    }

}
