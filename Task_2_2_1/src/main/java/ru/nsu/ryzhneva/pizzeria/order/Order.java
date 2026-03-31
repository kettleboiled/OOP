package ru.nsu.ryzhneva.pizzeria.order;

/**
 * Сущность заказа.
 */
public class Order {
    private final int numberOrder;
    private OrderState state;
    private final OrderStateListener stateListener;

    /**
     * Конструктор.
     *
     * @param numberOrder номер заказа.
     * @param stateListener слушатель изменений состояния.
     */
    public Order(int numberOrder, OrderStateListener stateListener) {
        this.numberOrder = numberOrder;
        this.stateListener = stateListener;
        this.state = OrderState.ORDERED;
        notifyListener();
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
     * Потокобезопасный метод получения состояния заказа.
     *
     * @return состояние заказа.
     */
    public synchronized OrderState getState() {
        return state;
    }

    /**
     * Продвигает заказ на следующий этап обработки.
     */
    public synchronized void advanceState() {
        if (this.state != OrderState.DELIVERED) {
            this.state = this.state.next();
            notifyListener();
        }
    }

    /**
     * Метод, оповещающий пользователя
     * об изменении состояния заказа.
     */
    private void notifyListener() {
        if (stateListener != null) {
            stateListener.onStateChanged(this);
        }
    }
}
