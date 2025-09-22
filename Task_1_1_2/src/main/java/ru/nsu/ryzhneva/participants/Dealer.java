package ru.nsu.ryzhneva.participants;

import ru.nsu.ryzhneva.gameinput.ConsoleView;
import ru.nsu.ryzhneva.cards.Card;
import ru.nsu.ryzhneva.cards.Deck;

/**
 * Класс, представляющий дилера.
 * Наследует Player и добавляет собственную игровую логику.
 */
public class Dealer extends Player {

    /**
     * Ход дилера: тянет карты, пока значение руки меньше 17.
     *
     * @param deck колода, из которой берутся карты
     */
    public void playTurn(Deck deck) {
        while (getValue() < 17) {
            Card card = deck.drawCard();
            addCard(card);
            ConsoleView.dealerDraw(card);

            // показываем состояние рук
            printString(false, false);
        }
    }
}

