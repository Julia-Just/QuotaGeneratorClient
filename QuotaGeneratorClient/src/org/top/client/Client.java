package org.top.client;

import java.net.InetAddress;
import java.net.Socket;
import java.util.Scanner;

import static org.top.Main.CONST_EXIT;

public class Client {


    public static void runClient(String serverIpAddrStr, int serverPort, Scanner scanner) {
        Socket remoteClient = null;     // сокет для работы с клиентом
        Sender sender = null;           // объект для отправки данных через сокет
        Receiver receiver = null;       // объект для чтения данных через сокет

        try {
            System.out.println("подключение к серверу: " + serverIpAddrStr);
            // 1. создать сокет клиента, подключенный к серверу
            remoteClient = new Socket(InetAddress.getByName(serverIpAddrStr), serverPort);
            System.out.println("клиент создан");

            // 2. создать объекты для общения
            sender = new Sender(remoteClient);
            receiver = new Receiver(remoteClient);

            // 3. алгоритм работы с клиентом
            while (true) {

                // 3.1) отправить ответ
                System.out.print( "введите сообщение: ");
                String msg = scanner.nextLine();
                sender.send(msg);                // отправить сообщение
                System.out.println("жду сообщения ...");
                if (msg.equals("exit")) {
                    System.out.println("завершение работы");
                    break;
                }
                // 3.2) принять сообщение
                msg = receiver.receive();        // считать сообщение

                System.out.println(msg);         // вывести сообщение

                if (msg.equals(CONST_EXIT)) {
                    System.out.println("завершение работы");
                    break;
                }
            }
        } catch (Exception e) {
            System.out.println("клиент> что-то пошло не так: " + e.getMessage());
        } finally {
            try {
                if (remoteClient != null && !remoteClient.isClosed()) {
                    remoteClient.close();
                }
                if (remoteClient != null && !remoteClient.isClosed()) {
                    remoteClient.close();
                }
                if (sender != null) {
                    sender.close();
                }
                if (receiver != null) {
                    receiver.close();
                }
            }
            catch (Exception ex) {
                System.out.println("клиент> что-то пошло не так в finally: " + ex.getMessage());
            }
        }
    }
    public static void JSONObject() {

    }

}
