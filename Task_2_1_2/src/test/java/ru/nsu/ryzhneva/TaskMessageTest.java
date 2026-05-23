package ru.nsu.ryzhneva;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import ru.nsu.ryzhneva.primalitytester.TaskRequest;
import ru.nsu.ryzhneva.primalitytester.TaskResponse;


/**
 * Тесты для TaskRequest и TaskResponse.
 */
class TaskMessageTest {

    @Test
    void taskRequestSerializationRoundTrip() throws Exception {
        TaskRequest request = new TaskRequest(42L, new int[]{2, 3, 5});

        byte[] data = serialize(request);
        TaskRequest restored = (TaskRequest) deserialize(data);

        Assertions.assertEquals(42L, restored.getTaskId());
        Assertions.assertArrayEquals(new int[]{2, 3, 5}, restored.getNumberChunk());
    }

    @Test
    void taskResponseSerializationRoundTrip() throws Exception {
        TaskResponse response = new TaskResponse(7L, true);

        byte[] data = serialize(response);
        TaskResponse restored = (TaskResponse) deserialize(data);

        Assertions.assertEquals(7L, restored.getTaskId());
        Assertions.assertTrue(restored.hasCompositeNumber());
    }

    private static byte[] serialize(Object obj) throws Exception {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        try (ObjectOutputStream oos = new ObjectOutputStream(out)) {
            oos.writeObject(obj);
        }
        return out.toByteArray();
    }

    private static Object deserialize(byte[] data) throws Exception {
        try (ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(data))) {
            return ois.readObject();
        }
    }
}

