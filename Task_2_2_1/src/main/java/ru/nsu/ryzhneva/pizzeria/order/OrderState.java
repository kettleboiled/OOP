package ru.nsu.ryzhneva.pizzeria.order;

/**
 * Состояния заказа с инкапсулированной логикой переходов.
 */
public enum OrderState {
    ORDERED {
        @Override
        public OrderState next() {
            return COOKING;
        }
    },
    COOKING {
        @Override
        public OrderState next() {
            return READY_SELECT_COURIER;
        }
    },
    READY_SELECT_COURIER {
        @Override
        public OrderState next() {
            return COMING;
        }
    },
    COMING {
        @Override
        public OrderState next() {
            return DELIVERED;
        }
    },
    DELIVERED {
        @Override
        public OrderState next() {
            return DELIVERED;
        }
    };

    /**
     * Возвращает следующее состояние в жизненном цикле заказа.
     *
     * @return следующее состояние.
     */
    public abstract OrderState next();
}