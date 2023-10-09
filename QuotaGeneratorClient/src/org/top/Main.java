package org.top;

import org.top.client.Client;
import java.util.Scanner;

public class Main {
    public static final String CONST_EXIT = "exit";

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);


        // главный цикл
        boolean isEnd = false;
        while (!isEnd) {
            System.out.println("1. Подключиться");
            System.out.println("2. Выход");
            System.out.print("Введите выбор: ");
            String choice = scanner.nextLine();
            //
            switch (choice) {
                case "1":
                    System.out.print("Введите ip-адрес своего сервера: ");
                    String ipStrSrv = scanner.nextLine();
                    System.out.print("Введите порт: ");
                    int portSrv = Integer.parseInt(scanner.nextLine());
                    Client.runClient(ipStrSrv, portSrv, scanner);
                case "2":
                    System.out.println("До свидания");
                    isEnd = true;
                    break;
                default:
                    System.out.println("Некорректный ввод. Повторите ещё раз");
            }
        }
    }
}