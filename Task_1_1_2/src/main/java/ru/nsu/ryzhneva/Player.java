package ru.nsu.ryzhneva;

import javax.swing.plaf.synth.SynthDesktopIconUI;
import java.util.ArrayList;
import java.util.List;

public class Player {

    List<Card> hand;

    public Player() {
        hand = new ArrayList<>();
    }

    public void addCard(Card card) {
        hand.add(card);
    }

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
     * @param closedCard  true — скрывать вторую карту дилера
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

    public List<Card> getHand() {
        return hand;
    }

}
