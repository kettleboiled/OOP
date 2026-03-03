package ru.nsu.ryzhneva;

import com.google.gson.Gson;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import ru.nsu.ryzhneva.pizzeria.PizzaProcess;
import ru.nsu.ryzhneva.pizzeria.PizzeriaConfig;


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

            PizzaProcess process = new PizzaProcess(config);
            process.work();
            System.out.println("Pizzeria started working");

            Thread.sleep(config.workTimeMs);

            System.out.println("Working time is over");
            process.close();

        } catch (IOException e) {
            System.err.println("Error IO: reading config.json failed: " + e.getMessage());
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}