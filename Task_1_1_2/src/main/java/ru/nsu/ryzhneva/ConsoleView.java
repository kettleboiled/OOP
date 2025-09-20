package ru.nsu.ryzhneva;

import java.util.List;

/**
 * Класс для отображения игрового интерфейса в консоли.
 * Содержит статические методы для вывода различных сообщений и состояния игры.
 */
public class ConsoleView{

    /**
     * Выводит приветственное сообщение при запуске игры.
     */
    public static void welcome() {
        System.out.println("Welcome to Blackjack!");
    }

    /**
     * Объявляет начало нового раунда игры.
     *
     * @param round номер текущего раунда
     */
    public static void roundStart(int round) {
        System.out.println("\nRound " + round);
        System.out.println("Dealer dealt the cards");
    }


    /**
     * Отображает карты указанного участника игры.
     *
     * @param owner "player" для игрока, "dealer" для дилера
     * @param cards список карт для отображения
     * @param closed если true, скрывает вторую карту (используется для дилера)
     * @param value текущее значение карт
     */
    public static void showCards(String owner, List<Card> cards, boolean closed, int value) {
        if (owner.equals("player")) {
            System.out.print("Your cards: [");
        } else {
            System.out.print("Dealer's cards: [");
        }

        if (closed && !cards.isEmpty()) {
            cards.get(0).print(false);
            System.out.print(", <hidden card>");
        } else {
            boolean overflow = value > 21;
            for (int i = 0; i < cards.size(); i++) {
                cards.get(i).print(overflow);
                if (i < cards.size() - 1) System.out.print(", ");
            }
        }

        System.out.print("]");
        if (!closed) {
            System.out.println(" => " + value);
        } else {
            System.out.println();
        }
    }

    /**
     * Объявляет начало хода игрока.
     */
    public static void playerTurn() {
        System.out.println("\nYour turn");
        System.out.println("-------");
    }

    /**
     * Объявляет начало хода дилера.
     */
    public static void dealerTurn() {
        System.out.println("\nDealer's turn");
        System.out.println("-------");
    }

    /**
     * Запрашивает у игрока действие (взять карту или остановиться).
     */
    public static void askAction() {
        System.out.println("Enter 1 to draw a card, or 0 to stand.");
    }

    /**
     * Сообщает о неверном вводе от пользователя.
     */
    public static void wrongAction() {
        System.out.println("Invalid input. Enter 1 or 0.");
    }

    /**
     * Сообщает о взятии карты игроком.
     *
     * @param card взятая карта
     */
    public static void playerDraw(Card card) {
        System.out.print("You drew ");
        card.print(false);
        System.out.println();
    }

    /**
     * Сообщает о взятии карты дилером.
     *
     * @param card взятая карта
     */

    public static void dealerDraw(Card card) {
        System.out.print("Dealer drew ");
        card.print(false);
        System.out.println();
    }

    /**
     * Сообщает о переборе у игрока (сумма карт превышает 21).
     */
    public static void playerBust() {
        System.out.println("You busted!");
    }

    /**
     * Сообщает что игрок набрал ровно 21 очко.
     */
    public static void player21() {
        System.out.println("You have 21, you stand.");
    }

    /**
     * Сообщает о переборе у дилера (сумма карт превышает 21).
     */

    public static void dealerBust() {
        System.out.println("Dealer busted. You win the round!");
    }

    /**
     * Сообщает о блэкджеке у обоих участников (комбинация из двух карт стоимостью 21).
     */
    public static void blackjackBoth() {
        System.out.println("Both got blackjack — it's a draw.");
    }

    /**
     * Сообщает о блэкджеке у игрока.
     */
    public static void blackjackPlayer() {
        System.out.println("Blackjack! You win the round!");
    }

    /**
     * Сообщает о блэкджеке у дилера.
     */
    public static void blackjackDealer() {
        System.out.println("Dealer has blackjack. You lose the round.");
    }

    /**
     * Сообщает о победе игрока в раунде.
     */
    public static void winPlayer() {
        System.out.println("You win the round!");
    }

    /**
     * Сообщает о победе дилера в раунде.
     */
    public static void winDealer() {
        System.out.println("Dealer wins the round.");
    }

    /**
     * Сообщает о ничьей в раунде.
     */
    public static void draw() {
        System.out.println("It's a draw.");
    }

    /**
     * Отображает текущий счет игры (количество побед игрока и дилера).
     *
     * @param player количество побед игрока
     * @param dealer количество побед дилера
     */
    public static void showScore(int player, int dealer) {
        System.out.println("Score " + player + ":" + dealer);
    }

    /**
     * Отображает финальный счет игры после завершения всех раундов.
     *
     * @param player количество побед игрока
     * @param dealer количество побед дилера
     */
    public static void finalScore(int player, int dealer) {
        System.out.println("Game over. Final score " + player + ":" + dealer);
    }

    /**
     * Запрашивает у пользователя желание сыграть еще один раунд.
     */
    public static void askAgain() {
        System.out.print("Do you want to play again? (y/n): ");
    }

    /**
     * Сообщает о создании новой колоды (когда текущая колода заканчивается).
     */
    public static void newDeck() {
        System.out.println("Deck is empty — creating a new one and shuffling.");
    }
}

