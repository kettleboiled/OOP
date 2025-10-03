package ru.nsu.ryzhneva.values;

import java.util.Map;
import ru.nsu.ryzhneva.Expression;

/**
 * Класс, представляющий переменную в математическом выражении.
 * Переменная хранит имя и может быть использована в вычислениях,
 * дифференцировании и упрощении выражений.
 */
public class Variable extends Expression {

    private final String name;

    /**
     * Конструктор для создания переменной с указанным именем.
     *
     * @param name Имя переменной.
     */
    public Variable(String name) {
        this.name = name;
    }

    /**
     * Возвращает имя переменной.
     *
     * @return Имя переменной.
     */
    public String getVar() {
        return name;
    }

    /**
     * Упрощает выражение для переменной.
     * Переменная не изменяется в процессе упрощения.
     *
     * @return Саму себя, так как переменная не может быть упрощена.
     */
    @Override
    public Expression funcSimple() {
        return this;
    }

    /**
     * Выполняет дифференцирование переменной по заданному имени.
     * Если переменная совпадает с заданным именем,
     * возвращается единица, иначе ноль.
     *
     * @param varName Имя переменной, по которой
     * производится дифференцирование.
     * @return Новое выражение,
     * представляющее собой производную переменной.
     */
    @Override
    public Expression derivative(String varName) {
        return new Number(this.name.equals(varName) ? 1 : 0);
    }

    /**
     * Вычисляет значение переменной,
     * подставив её значение из карты переменных.
     *
     * @param variables Карта, содержащая значения переменных.
     * @return Значение переменной.
     * @throws IllegalArgumentException Если переменная
     * не найдена в карте.
     */
    @Override
    public double eval(Map<String, Double> variables) {
        if (!variables.containsKey(name)) {
            throw new IllegalArgumentException("Значение для переменной '"
                    + name + "' не задано.");
        }
        return variables.get(name);
    }

    /**
     * Возвращает строковое представление переменной.
     *
     * @return Имя переменной в виде строки.
     */
    @Override
    public String print() {
        return name;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        Variable variable = (Variable) obj;
        return name.equals(variable.name);
    }

    @Override
    public int hashCode() {
        return name.hashCode();
    }

}