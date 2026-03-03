package ru.nsu.ryzhneva.pizzeria;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import ru.nsu.ryzhneva.pizzeria.order.Order;
import ru.nsu.ryzhneva.pizzeria.workers.Baker;
import ru.nsu.ryzhneva.pizzeria.workers.Courier;


/**
 * Управляющий класс - жизненный цикл симулятора пиццерии.
 */
public class PizzaProcess {
    private final PizzeriaConfig pizzeriaConfig;
    private final ThreadSafeQueue<Order> orders = new ThreadSafeQueue<>(100);
    private final ThreadSafeQueue<Order> warehouse;

    private final List<Thread> bakerThreads = new ArrayList<>();
    private final List<Thread> courierThreads = new ArrayList<>();
    private Thread orderProcess;

    private final AtomicInteger orderIdCreator = new AtomicInteger(1);

    /**
     * Инициализирует системные компоненты
     * на основе предоставленной конфигурации.
     *
     * @param pizzeriaConfig параметры инициализации.
     */
    public PizzaProcess(PizzeriaConfig pizzeriaConfig) {
        this.pizzeriaConfig = pizzeriaConfig;
        this.warehouse = new ThreadSafeQueue<>(pizzeriaConfig.warehouseSize);
    }

    /**
     * Запускает производственный процесс.
     * Инициализирует пулы потоков пекарей и курьеров,
     * а также поток-генератор заказов.
     */
    public void work() {
        for (int i = 0; i < pizzeriaConfig.bakersCount; i++) {
            int speedBaker =
                    pizzeriaConfig.bakerSpeeds[i % pizzeriaConfig.bakerSpeeds.length];
            Thread t = new Thread(new Baker(i, speedBaker, orders, warehouse));
            t.start();
            bakerThreads.add(t);
        }
        for (int i = 0; i < pizzeriaConfig.couriersCount; i++) {
            int trunkVolume =
                    pizzeriaConfig.couriersTrunkVolume
                            [i % pizzeriaConfig.couriersTrunkVolume.length];
            Thread t = new Thread(new Courier(i, trunkVolume, 2000, warehouse));
            t.start();
            courierThreads.add(t);
        }

        orderProcess = new Thread(() -> {
            try {
                while (!Thread.currentThread().isInterrupted()) {
                    orders.put(new Order(orderIdCreator.getAndIncrement()));
                    Thread.sleep(1000);
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });

        orderProcess.start();
    }

    /**
     * Останавливает работу пиццерии: сначала останавливает
     * прием заказов, пекари завешают текущие заказы и
     * курьеры их доставляют, все завершают работу.
     */
    public void close() {
        System.out.println("Stop accepting orders");

        if (orderProcess != null) {
            orderProcess.interrupt();
        }

        orders.close();

        for (Thread baker : bakerThreads) {
            try {
                baker.join();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }

        System.out.println("All bakers finished. Closing warehouse...");

        warehouse.close();

        for (Thread courier : courierThreads) {
            try {
                courier.join();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }

        System.out.println("All couriers finished");
    }
}
