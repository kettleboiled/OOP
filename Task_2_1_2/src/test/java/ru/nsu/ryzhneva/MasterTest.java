package ru.nsu.ryzhneva;

import java.net.InetSocketAddress;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import ru.nsu.ryzhneva.primalitytester.Master;

/**
 * Тесты для Master.
 */
class MasterTest {

    @Test
    void constructorRejectsEmptyWorkers() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> new Master(List.of()));
    }

    @Test
    void constructorRejectsNullWorkers() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> new Master(null));
    }

    @Test
    void throwsWhenAllWorkersUnavailable() {
        Master master = new Master(List.of(new InetSocketAddress("127.0.0.1", 65000)));
        int[] data = {7};

        Assertions.assertThrows(IllegalStateException.class, () -> master.hasComposite(data));
    }
}

