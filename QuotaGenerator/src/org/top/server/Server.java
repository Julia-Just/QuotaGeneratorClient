package org.top.server;

import org.top.communication.Sender;
import org.top.quotagen.IGenerator;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

// Класс Сервера - реализует логику сервера
public class Server {

    // поля
    private final String ipStr;           // адрес сервера
    private final int port;               // порт сервера
    private final int limit;              // максимльное кол-во входящих подключений
    //
    private ClientProcessor[] processors; // массив обработчиков клиентов
    // для многопоточности
    private ExecutorService threadPool = null; // пул потоков

    // конструктор с параметрами
    public Server(String ipStr, int port, int limit, IGenerator generator) {
        // инициализация информации сервера
        this.ipStr = ipStr;
        this.port = port;
        this.limit = limit + 1;     // одно подключение для специальных ответов

        // создадим обработчики
        threadPool = Executors.newFixedThreadPool(limit);   // пул потоков
        processors = new ClientProcessor[limit];            // не увеличинный лимит
        for (int i = 0; i < processors.length; i++) {
            processors[i] = new ClientProcessor(generator);  // создали пустые обработчики
        }
    }

    /*  Алгоритм работы сервера:
            - метод сервера ожидает входящие подключения (вечный цикл)
            - при подключении очередного клиента, сервер ищет свободного обработчика для этого клиента
            - если свободный обработчик отсутствует, то клиенту отправляется соответствующий ответ и соединение разрывается
            - иначе запускается метод работы с клиентом в отдельном потоке через обработчик
            - при завершении работы с клиентом, он отключается. */

    // метод работы сервера
    public void run() throws IOException {
        ServerSocket server = null; // сокет сервера

        try {
            System.out.println(getPrefix() + " Запуск сервера ...");
            // создаем экземпляр сокета сервера
            server = new ServerSocket(port, limit, InetAddress.getByName(ipStr));
            System.out.println(getPrefix() + " сервер запущен");

            // цикл работы сервера: подключать клиентов и запускать потоки на них
            while (true) {
                System.out.println(getPrefix() + " ожидаю входящего подключения ...");
                Socket nextClient = server.accept();    // тут подключился очередной клиент
                System.out.println(getPrefix() + " соединение " +
                        nextClient.getInetAddress().getHostAddress() + ":" + nextClient.getPort());

                // получить свободного исполнителя
                ClientProcessor processor = getFreeProcessor();
                if (processor != null) {
                    // если есть свободный, то запустить его
                    processor.prepareClient(nextClient);
                    // запуск метода processor.processClient в отдельном потоке
                    threadPool.execute(() -> {
                        try {
                            processor.processClient();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    });
                } else {
                    // если нет свободного обработчика
                    // отправить отрицательный ответ клиенту
                    Sender sender = new Sender(nextClient);
                    sender.sendMsg("Нет свободного сокета, вы будете отключены :c");
                    sender.close();
                    nextClient.close();
                }
            }
        }
        catch (Exception ex) {
            System.out.println("Ошибка сервера: " + ex.getMessage());
        }
        finally {
            if (server != null && !server.isClosed()) {
                server.close();
            }
        }
    }

    // метод получения свободного исполнителя
    private ClientProcessor getFreeProcessor() {
        for (ClientProcessor processor: processors) {
            if (processor.isFree()) {
                return processor;
            }
        }
        return null;
    }

    // вспомогательный метод префикса сервера
    private String getPrefix() {
        return "Сервер " + ipStr + ":" + port + "> ";
    }
}
