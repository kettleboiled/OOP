package ru.nsu.ryzhneva.values;

import java.util.Map;
import ru.nsu.ryzhneva.Expression;

/**
 * Класс, представляющий числовое значение в математическом выражении.
 * Число может быть использовано в вычислениях, дифференцировании и упрощении выражений.
 */
public class Number extends Expression {

    private final double value;

    /**
     * Конструктор для создания числа с указанным значением.
     *
     * @param value Значение числа.
     */
    public Number(double value) {
        this.value = value;
    }

    /**
     * Возвращает значение числа.
     *
     * @return Значение числа.
     */
    public double getValue() {
        return value;
    }

    /**
     * Упрощает выражение для числа.
     * Число не изменяется в процессе упрощения.
     *
     * @return Само число, так как оно не может быть упрощено.
     */
    @Override
    public Expression funcSimple() {
        return this;
    }

    /**
     * Выполняет дифференцирование числа.
     * Производная от числа всегда равна нулю.
     *
     * @param varName Имя переменной, по которой производится дифференцирование.
     * @return Число, представляющее собой производную (всегда 0).
     */
    @Override
    public Expression derivative(String varName) {
        return new Number(0);
    }

    /**
     * Вычисляет значение числа. Число не зависит от переменных.
     *
     * @param variables Карта, содержащая значения переменных.
     * @return Значение числа.
     */
    @Override
    public double eval(Map<String, Double> variables) {
        return value;
    }

    /**
     * Возвращает строковое представление числа.
     * Если число является целым, выводится без десятичной части.
     *
     * @return Строковое представление числа.
     */
    @Override
    public String print() {
        if (value == (int) value) {
            return String.format("%d", (int) value);
        }
        return String.valueOf(value);
    }

    /**
     * Сравнение операций.
     *
     * @param obj Объект для сравнения.
     * @return {@code true}, если объекты равны, иначе {@code false}.
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        Number number = (Number) obj;
        return Double.compare(number.value, value) == 0;
    }

    /**
     * Возвращает хэш-код для данной операции.
     *
     * @return Целочисленный хэш-код.
     */
    @Override
    public int hashCode() {
        return Double.hashCode(value);
    }
}