package ru.nsu.ryzhneva.dsl.builders

import ru.nsu.ryzhneva.dsl.ClosureProcessor
import ru.nsu.ryzhneva.domain.Checkpoint

import java.time.LocalDate

/**
 * Строитель (Builder) для создания
 * контрольных точек (Checkpoint) в рамках системы DSL.
 * Позволяет настраивать параметры контрольной точки, такие как дата.
 */
class CheckpointBuilder {
    private final Checkpoint checkpoint

    private CheckpointBuilder(String name) {
        this.checkpoint = new Checkpoint()
        this.checkpoint.setName(name)
    }

    /**
     * Создает объект Checkpoint и применяет
     * к нему замыкание для настройки свойств.
     * @param name имя контрольной точки
     * @param closure Groovy-замыкание, содержащее шаги конфигурации
     * @return готовый объект Checkpoint
     */
    static Checkpoint build(String name, Closure closure) {
        CheckpointBuilder builder = new CheckpointBuilder(name)
        ClosureProcessor.apply(builder, closure)
        return builder.checkpoint
    }

    /**
     * Устанавливает дату контрольной точки.
     * @param date дата в формате YYYY-MM-DD
     */
    void setDate(String date) {
        this.checkpoint.setDate(LocalDate.parse(date))
    }
}
