package ru.nsu.ryzhneva.dsl

/**
 * Вспомогательный класс для работы с Groovy-замыканиями.
 * Перенастраивает вызовы внутри замыкания (Closure) на целевой объект (delegateObj).
 */
class ClosureProcessor {

    /**
     * Создает копию замыкания, устанавливает
     * получателем вызовов ({delegate}) переданный объект-делегат.
     * Затем инициирует выполнение замыкания для конфигурации объекта.
     *
     * @param delegateObj объект, к которому будут применяться вызовы внутри замыкания
     * @param closure Groovy-замыкание,
     * содержащее пользовательский код для инициализации объекта
     */
    static void apply(Object delegateObj, Closure closure) {
        Closure cloned = (Closure) closure.clone()
        cloned.delegate = delegateObj
        cloned.resolveStrategy = Closure.DELEGATE_FIRST
        cloned.call()
    }
}
