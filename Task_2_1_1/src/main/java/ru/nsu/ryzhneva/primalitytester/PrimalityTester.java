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
     * @return true - простое;
     * false - cоставное или < 2.
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

        int sqrt = (int)Math.sqrt(number);
        for (int i = 3; i <= sqrt; i+=2) {
            if (number % i == 0) {
                return false;
            }
        }
        return true;
    }

    /**
     * Есть ли в массиве составное число.
     *
     * @param arr массив.
     * @return true - найдено составное число;
     * false - все числа в массиве простые или массив пуст.
     */
    boolean hasComposite(int[] arr);
}
