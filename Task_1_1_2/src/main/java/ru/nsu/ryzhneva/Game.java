package ru.nsu.ryzhneva;

import java.util.Scanner;

/**
 * Класс, управляющий логикой игры Блэкджек.
 * Содержит основную игровую логику, управление раундами и подсчет очков.
 */
public class Game {

    Deck deck;
    Player player;
    Player dealer;
    Scanner in = new Scanner(System.in);

    int countRounds = 0;
    int winPlayer = 0;
    int winDealer = 0;

    public Game() {
        newDeck();
    }

    /**
     * Создает новую колоду и перемешивает ее.
     */
    private void newDeck() {
        deck = new Deck();
        deck.shuffle();
    }

    /**
     * Запускает основной игровой цикл.
     * Управляет раундами игры, запрашивает повтор игры у пользователя.
     */
    public void start() {
        ConsoleView.welcome();

        boolean again = true;
        while (again) {
            countRounds++;
            ConsoleView.roundStart(countRounds);
            playRound();

            ConsoleView.showScore(winPlayer, winDealer);

            ConsoleView.askAgain();
            String ans = in.next().trim().toLowerCase();
            again = ans.equals("y");
        }

        ConsoleView.finalScore(winPlayer, winDealer);
    }

    /**
     * Проверяет наличие блэкджека у игрока или дилера.
     * Блэкджек - комбинация из двух карт общей стоимостью 21.
     *
     * @return true если у кого-то из участников блэкджек, иначе false
     */
    boolean checkBlackjack() {
        boolean playerBlackjack = (player.getValue() == 21 && player.hand.size() == 2);
        boolean dealerBlackjack = (dealer.getValue() == 21 && dealer.hand.size() == 2);

        if (playerBlackjack || dealerBlackjack) {
            System.out.println("\nDistribution result: ");
            player.printString(true, false);
            dealer.printString(false, false);

            if (playerBlackjack && dealerBlackjack) {
                ConsoleView.blackjackBoth();
            } else if (playerBlackjack) {
                ConsoleView.blackjackPlayer();
                winPlayer++;
            } else {
                ConsoleView.blackjackDealer();
                winDealer++;
            }
            return true;
        }
        return false;
    }

    /**
     * Проверяет, набрал ли игрок 21 очко.
     *
     * @return true если у игрока 21 очко, иначе false
     */
    boolean checkPlayerTurn() {
        if (player.getValue() == 21) {
            ConsoleView.player21();
            return true;
        }
        return false;
    }

    /**
     * Проверяет, превысил ли игрок лимит в 21 очко (перебор).
     *
     * @return true если у игрока перебор, иначе false
     */
    boolean checkBust() {
        if (player.getValue() > 21) {
            ConsoleView.playerBust();
            winDealer++;
            return true;
        }
        return false;
    }

    /**
     * Сравнивает итоговые очки игрока и дилера и определяет победителя.
     *
     * @param p очки игрока
     * @param d очки дилера
     */
    void compareFinal(int p, int d) {
        if (d > 21) {
            ConsoleView.dealerBust();
            winPlayer++;
        } else if (p > d) {
            ConsoleView.winPlayer();
            winPlayer++;
        } else if (p < d) {
            ConsoleView.winDealer();
            winDealer++;
        } else {
            ConsoleView.draw();
        }
    }

    /**
     * Безопасный розыгрыш.
     * Если колода пуста, создается новая перетасованная колода.
     *
     * @return взятая карта
     */
    private Card safeDraw() {
        if (deck.isEmpty()) {
            ConsoleView.newDeck();
            newDeck();
        }
        return deck.drawCard();
    }

    /**
     * Проводит один раунд игры Блэкджек.
     * Включает раздачу карт, ходы игрока и дилера, определение победителя.
     */
    private void playRound() {
        player = new Player();
        dealer = new Player();

        // первоначальная раздача: игроку 2 карты, дилеру 2 карты (одна закрытая)
        player.addCard(safeDraw());
        player.addCard(safeDraw());
        dealer.addCard(safeDraw());
        dealer.addCard(safeDraw());

        player.printString(true, false);
        dealer.printString(false, true);

        // Проверьте начальный блэкджек
        if (checkBlackjack()) {
            return;
        }

        ConsoleView.playerTurn();
        while (true) {
            if (checkBust()) {
                return;
            }
            if (checkPlayerTurn()) {
                return;
            }

            ConsoleView.askAction();
            int input;
            try {
                input = in.nextInt();
            } catch (Exception e) {
                in.nextLine();
                ConsoleView.wrongAction();
                continue;
            }

            if (input == 1) {
                Card card = safeDraw();
                player.addCard(card);
                ConsoleView.playerDraw(card);
            } else {
                break;
            }

            player.printString(true, false);
            dealer.printString(false, true);
        }

        // Проверка на перебор после последнего розыгрыша
        if (player.getValue() > 21) {
            ConsoleView.playerBust();
            winDealer++;
            return;
        }

        ConsoleView.dealerTurn();
        dealer.printString(false, false);

        // Розыгрыш дилера при значении < 17
        while (dealer.getValue() < 17) {
            Card card = safeDraw();
            dealer.addCard(card);
            ConsoleView.dealerDraw(card);

            player.printString(true, false);
            dealer.printString(false, false);
        }

        // Результат
        int p = player.getValue();
        int d = dealer.getValue();

        compareFinal(p, d);
    }
}
