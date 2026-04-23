package ru.nsu.ryzhneva;

import java.io.File;
import java.io.IOException;

/**
 * Утилитарный класс для вызова консольных команд.
 * Отвечает за запуск процессов операционной системы в заданной директории, 
 * перехватывая их вывод (наследуя IO) и управляя переменными окружения.
 */
public class CommandExecutor {

    /**
     * Выполняет заданную консольную команду.
     *
     * @param directory директория, где будет выполнена команда
     * @param command сама команда и ее аргументы
     * @return true - если команда завершилась с кодом 0, иначе false
     */
    public boolean execute(File directory, String... command) {
        ProcessBuilder pb = new ProcessBuilder(command);
        pb.directory(directory);
        pb.environment().put("GIT_TERMINAL_PROMPT", "0");
        pb.inheritIO();
        try {
            return pb.start().waitFor() == 0;
        } catch (IOException | InterruptedException e) {
            System.err.println("Execution failed: " + String.join(" ", command));
            return false;
        }
    }
}
