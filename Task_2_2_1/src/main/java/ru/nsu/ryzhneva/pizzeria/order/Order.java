package ru.nsu.ryzhneva.pizzeria.order;

/**
 * Сущность заказа.
 */
public class Order {
    private final int numberOrder;
    private OrderState state;

    /**
     * Конструктор.
     *
     * @param numberOrder номер заказа.
     */
    public Order(int numberOrder) {
        this.numberOrder = numberOrder;
        this.state = OrderState.ORDERED;
    }

    /**
     * Получить номер заказа.
     *
     * @return номер заказа.
     */
    public int getId() {
        return numberOrder;
    }

    /**
     * Потокобезопасный метод изменения состояния заказа.
     *
     * @param state новое целевое состояние заказа.
     */
    public synchronized void setState(OrderState state) {
        this.state = state;
        System.out.println("[" + numberOrder + "] [" + state + "]");
    }
}
