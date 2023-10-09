package org.top.communication;

import java.io.*;
import java.net.Socket;

// класс для отправки данных через сокет
public class Sender implements Closeable {
    private PrintWriter out = null; // поток для отправки данных

    // конструктор
    public Sender(Socket socket) throws IOException {
        out = new PrintWriter(
                new BufferedWriter(
                        new OutputStreamWriter(socket.getOutputStream())
                ),
                true
        );
    }

    // закрытие
    @Override
    public void close() {
        if (out != null) {
            out.close();
            out = null;
        }
    }

    // отправка сообщения
    public void sendMsg(String msg) {
        // TODO: добавить выброс исключения при out == null (IOException)
        if (out != null) {
            out.println(msg);
        }
    }
}
