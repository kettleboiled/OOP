package ru.nsu.ryzhneva.pizzeria.order;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;

/**
 * Тесты для класса Order.
 */
class OrderTest {

    @Test
    void testOrderInitialization() {
        List<OrderState> loggedStates = new ArrayList<>();
        Order order = new Order(1, o -> loggedStates.add(o.getState()));
        assertEquals(1, order.getId());
        assertEquals(OrderState.ORDERED, order.getState());
        assertEquals(1, loggedStates.size());
        assertEquals(OrderState.ORDERED, loggedStates.get(0));
    }

    @Test
    void testOrderStateAdvancement() {
        Order order = new Order(2, null);
        assertEquals(OrderState.ORDERED, order.getState());
        order.advanceState();
        assertEquals(OrderState.COOKING, order.getState());
        order.advanceState();
        assertEquals(OrderState.READY_SELECT_COURIER, order.getState());
        order.advanceState();
        assertEquals(OrderState.COMING, order.getState());
        order.advanceState();
        assertEquals(OrderState.DELIVERED, order.getState());
        order.advanceState();
        assertEquals(OrderState.DELIVERED, order.getState()); // Should not go past DELIVERED
    }

    @Test
    void testOrderStateNext() {
        assertEquals(OrderState.COOKING, OrderState.ORDERED.next());
        assertEquals(OrderState.READY_SELECT_COURIER, OrderState.COOKING.next());
        assertEquals(OrderState.COMING, OrderState.READY_SELECT_COURIER.next());
        assertEquals(OrderState.DELIVERED, OrderState.COMING.next());
        assertEquals(OrderState.DELIVERED, OrderState.DELIVERED.next());
    }
}

