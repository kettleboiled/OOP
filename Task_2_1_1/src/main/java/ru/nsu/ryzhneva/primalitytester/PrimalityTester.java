package ru.nsu.ryzhneva.primalitytester;

/**
 * Интерфейс, определяющий контракт
 * для проверки массива чисел на наличие составных чисел.
 */
public interface PrimalityTester {

    /**
     * Проверяет, является ли число простым.
     *
     * @param number число для проверки.
     * @return true, если число простое;
     * false, если число составное,
     * либо меньше 2 (0, 1, отрицательные).
     */
    default boolean isPrime(int number) {
        if (number < 2) {
            return false;
        }
        if (number == 2 || number == 3) {
            return true;
        }
        if (number % 2 == 0) {
            return false;
        }

        int sqrt = (int) Math.sqrt(number);
        for (int i = 3; i <= sqrt; i += 2) {
            if (number % i == 0) {
                return false;
            }
        }
        return true;
    }

    /**
     * Есть ли в массиве составное число.
     *
     * @param arr входной массив целых чисел.
     * @return true, если найдено число, не являющееся простым;
     * false, если все числа в массиве простые или массив пуст.
     */
    boolean hasComposite(int[] arr);
}
