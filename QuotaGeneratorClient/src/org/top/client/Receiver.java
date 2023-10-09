package org.top.client;

import java.io.BufferedReader;
import java.io.Closeable;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

public class Receiver implements Closeable {
    private BufferedReader in;  // поток чтения

    public Receiver(Socket socket) throws IOException {
        // создать поток из сокета
        in = new BufferedReader(
                new InputStreamReader(socket.getInputStream())
        );
    }

    // метод чтения данных из потока
    public String receive() throws IOException {
        if (in == null) {
            throw new IOException("stream 'in' is null, maybe it already closed");
        }
        return in.readLine();
    }

    @Override
    public void close() throws IOException {
        if (in != null) {
            in.close();
            in = null;
        }
    }
}
