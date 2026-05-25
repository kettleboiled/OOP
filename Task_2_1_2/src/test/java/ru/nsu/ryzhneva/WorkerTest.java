package ru.nsu.ryzhneva;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import org.junit.jupiter.api.Test;
import ru.nsu.ryzhneva.primalitytester.SequentialPrimalityTester;
import ru.nsu.ryzhneva.primalitytester.Worker;

/**
 * Тесты для Worker.
 */
class WorkerTest {

    @Test
    void workerHandlesMalformedInput() throws Exception {
        int port = findFreePort();
        startWorker(port);

        try (Socket socket = new Socket()) {
            socket.connect(new InetSocketAddress("127.0.0.1", port), 300);
            socket.getOutputStream().write(new byte[]{1, 2, 3, 4});
            socket.getOutputStream().flush();
        }
    }

    @Test
    void workerHandlesClientDisconnect() throws Exception {
        int port = findFreePort();
        startWorker(port);

        try (Socket socket = new Socket()) {
            socket.connect(new InetSocketAddress("127.0.0.1", port), 300);
        }
    }

    private static void startWorker(int port) throws Exception {
        Worker worker = new Worker(port, new SequentialPrimalityTester());
        Thread thread = new Thread(worker::start, "worker-test-" + port);
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
