package ru.nsu.ryzhneva.pizzeria.order;

/**
 * Интерфейс для прослушивания изменений состояния заказа.
 */
public interface OrderStateListener {

    /**
     * Метод для изменения состояния заказа.
     *
     * @param order заказ.
     */
    void onStateChanged(Order order);
}