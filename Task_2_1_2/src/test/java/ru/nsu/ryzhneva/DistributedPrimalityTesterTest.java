package ru.nsu.ryzhneva;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import ru.nsu.ryzhneva.primalitytester.Main;
import ru.nsu.ryzhneva.primalitytester.Master;
import ru.nsu.ryzhneva.primalitytester.SequentialPrimalityTester;
import ru.nsu.ryzhneva.primalitytester.Worker;


/**
 * Тесты для Worker.
 */
class DistributedPrimalityTesterTest {

    @Test
    void masterWorkerFindsComposite() throws Exception {
        int port = findFreePort();
        startWorker(port);

        Master master = new Master(List.of(
                new InetSocketAddress("127.0.0.1", port)));
        int[] input = {6, 8, 7, 13, 5, 9, 4};

        Assertions.assertTrue(master.hasComposite(input));
    }

    @Test
    void masterWorkerAllPrimesFalse() throws Exception {
        int port = findFreePort();
        startWorker(port);

        Master master = new Master(List.of(
                new InetSocketAddress("127.0.0.1", port)));
        int[] input = {
                20319251, 6997901, 6997927, 6997937, 17858849, 6997967,
                6998009, 6998029, 6998039, 20165149, 6998051, 6998053
        };

        Assertions.assertFalse(master.hasComposite(input));
    }

    @Test
    void mainMasterModeRuns() throws Exception {
        int port = findFreePort();
        startWorker(port);

        String[] args = {
                "master",
                "127.0.0.1:" + port,
                "6", "8", "7", "13", "5", "9", "4"
        };

        PrintStream originalOut = System.out;
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        System.setOut(new PrintStream(out));
        try {
            Main.main(args);
        } finally {
            System.setOut(originalOut);
        }

        Assertions.assertTrue(out.toString().contains("Результат:"));
    }

    private static void startWorker(int port) throws Exception {
        Worker worker = new Worker(port, new SequentialPrimalityTester());
        Thread thread = new Thread(worker::start, "worker-" + port);
        thread.setDaemon(true);
        thread.start();
        waitForPortOpen("127.0.0.1", port, 3000);
    }

    private static int findFreePort() throws IOException {
        try (ServerSocket serverSocket = new ServerSocket(0)) {
            return serverSocket.getLocalPort();
        }
    }

    private static void waitForPortOpen(String host, int port, long timeoutMs) throws Exception {
        long deadline = System.currentTimeMillis() + timeoutMs;
        while (System.currentTimeMillis() < deadline) {
            try (Socket socket = new Socket()) {
                socket.connect(new InetSocketAddress(host, port), 200);
                return;
            } catch (IOException ex) {
                Thread.sleep(50);
            }
        }
        throw new IllegalStateException("Worker did not start in time");
    }
}
