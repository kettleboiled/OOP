package ru.nsu.ryzhneva;

/**
 * Исключение, выбрасываемое при ошибках чтения/разбора DSL-конфигурации курса.
 */
public class ConfigParseException extends Exception {

    /**
     * Создает исключение с сообщением.
     *
     * @param message текст ошибки
     */
    public ConfigParseException(String message) {
        super(message);
    }

    /**
     * Создает исключение с сообщением и причиной.
     *
     * @param message текст ошибки
     * @param cause исходная причина
     */
    public ConfigParseException(String message,
                                Throwable cause) {
        super(message, cause);
    }
}

