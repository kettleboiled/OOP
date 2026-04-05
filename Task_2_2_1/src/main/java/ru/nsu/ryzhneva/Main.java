package ru.nsu.ryzhneva;

import com.google.gson.Gson;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import ru.nsu.ryzhneva.pizzeria.PizzaProcess;
import ru.nsu.ryzhneva.pizzeria.PizzeriaConfig;
import ru.nsu.ryzhneva.pizzeria.order.Order;
import ru.nsu.ryzhneva.pizzeria.order.OrderGenerator;
import ru.nsu.ryzhneva.pizzeria.order.OrderStateListener;
import ru.nsu.ryzhneva.pizzeria.queue.BoundedThreadSafeQueue;
import ru.nsu.ryzhneva.pizzeria.queue.UnboundedThreadSafeQueue;
import ru.nsu.ryzhneva.pizzeria.workers.StaffFactory;
import ru.nsu.ryzhneva.pizzeria.workers.WorkerGroup;


/**
 * Класс Main.
 */
public class Main {
    /**
     * Точка входа.
     *
     * @param args аргументы.
     */
    public static void main(String[] args) {
        Gson gson = new Gson();
        try (Reader reader = new FileReader("config.json")) {
            PizzeriaConfig config = gson.fromJson(reader, PizzeriaConfig.class);
            System.out.println("Configuration successful loaded");

            UnboundedThreadSafeQueue<Order> ordersQueue
                    = new UnboundedThreadSafeQueue<>();
            BoundedThreadSafeQueue<Order> warehouseQueue
                    = new BoundedThreadSafeQueue<>(config.warehouseSize());

            WorkerGroup bakers = StaffFactory.createBakers(config, ordersQueue, warehouseQueue);
            WorkerGroup couriers = StaffFactory.createCouriers(config, warehouseQueue);

            OrderStateListener logger = order ->
                    System.out.println("[" + order.getId() + "] [" + order.getState() + "]");

            OrderGenerator generator = new OrderGenerator(ordersQueue, 1000, logger);
            Thread genThread = new Thread(generator);

            PizzaProcess process = new PizzaProcess(
                    ordersQueue, warehouseQueue, bakers, couriers, genThread);

            process.work();
            System.out.println("Pizzeria started working");

            Thread.sleep(config.workTimeMs());

            System.out.println("Working time is over");
            process.close();

        } catch (IOException e) {
            System.err.println("Error IO: reading config.json failed: " + e.getMessage());
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}