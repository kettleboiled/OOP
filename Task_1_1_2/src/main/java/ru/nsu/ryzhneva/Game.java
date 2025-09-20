package ru.nsu.ryzhneva;

import java.util.Scanner;

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

    private void newDeck() {
        deck = new Deck();
        deck.shuffle();
    }

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

    boolean checkPlayerTurn() {
        if (player.getValue() == 21) {
            ConsoleView.player21();
            return true;
        }
        return false;
    }

    boolean checkBust() {
        if (player.getValue() > 21) {
            ConsoleView.playerBust();
            winDealer++;
            return true;
        }
        return false;
    }

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
     * Безопасный розыгрыш: если колода пуста, создайте новую перетасованную колоду (правила использования одной колоды).
     *  * Возвращает взятую карту (никогда не обнуляйте, если только что-то не пошло не так).
     */
    private Card safeDraw() {
        if (deck.isEmpty()) {
            ConsoleView.newDeck();
            newDeck();
        }
        return deck.drawCard();
    }

    private void playRound() {
        player = new Player();
        dealer = new Player();

        // первоначальная раздача: игроку 2 карты, дилеру 2 карты (одна закрытая).
        player.addCard(safeDraw());
        player.addCard(safeDraw());
        dealer.addCard(safeDraw());
        dealer.addCard(safeDraw());

        player.printString(true, false);
        dealer.printString(false, true);


        // Проверьте начальный блэкджек (сумма двух карт равна 21 очку)
        if (checkBlackjack()) return;

        ConsoleView.playerTurn();
        while (true) {
            if(checkBust()) return;
            if (checkPlayerTurn()) return;

            ConsoleView.askAction();
            int input;
            try {
                input = in.nextInt();
            }
            catch (Exception e) {
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

        // Если игрок проиграл после последнего розыгрыша
        if (player.getValue() > 21) {
            ConsoleView.playerBust();
            winDealer++;
            return;
        }

        ConsoleView.dealerTurn();

        dealer.printString(false, false);

        // дилер делает розыгрыш при значении < 17 (правило: делайте розыгрыш при <=16; остановитесь при >=17)
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
