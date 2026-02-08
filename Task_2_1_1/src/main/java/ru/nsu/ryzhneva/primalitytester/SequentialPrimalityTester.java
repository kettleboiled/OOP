package ru.nsu.ryzhneva.primalitytester;

/**
 * Рализация интерфейса {@link PrimalityTester},
 * выполняющая проверку последовательно
 * в одном потоке.
 */
public class SequentialPrimalityTester implements PrimalityTester {

    /**
     * Последовательно перебирает элементы массива
     * и проверяет их на простоту.
     *
     * @param arr массив.
     * @return true - найдено составное число,
     * иначе false.
     */
    @Override
    public boolean hasComposite(int[] arr) {
        for (int number: arr) {
            if (!isPrime(number)) {
                return true;
            }
        }
        return false;
    }
}
