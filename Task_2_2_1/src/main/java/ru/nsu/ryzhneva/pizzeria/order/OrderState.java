package ru.nsu.ryzhneva.pizzeria.order;

/**
 * Состояния заказа.
 */
public enum OrderState {
    ORDERED,
    COOKING,
    READY_SELECT_COURIER,
    COMING,
    DELIVERED
}
