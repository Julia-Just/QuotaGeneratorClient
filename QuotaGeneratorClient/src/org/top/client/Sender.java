package org.top.client;

import java.io.*;
import java.net.Socket;

public class Sender implements Closeable {
    private PrintWriter out;    // поток записи

    // конструктор
    public Sender(Socket socket) throws IOException {
        // создать поток из сокета
        out = new PrintWriter(
                new BufferedWriter(
                        new OutputStreamWriter(socket.getOutputStream())
                ),
                true
        );
    }

    // метод отправки строки в поток сокета
    public void send(String message) throws IOException {
        if (out == null) {
            // если поток был закрыт ранее
            throw new IOException("stream 'out' is null, maybe it already closed");
        }
        out.println(message);
    }


    @Override
    public void close() throws IOException {
        if (out != null) {
            out.close();
            out = null;
        }
    }
}
