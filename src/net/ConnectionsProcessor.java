package net;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

/**Класс ищущий новые подключения, регистрирующий их,
 *И создающий потоки для работы с ними
 */
public class ConnectionsProcessor implements Runnable {
    private static ArrayList<ClientThread> users;
    private ServerSocket socket;

    public static ArrayList<ClientThread> getUsers() {
        return users;
    }

    /**Инициализируем сокет на порту 1234 **/
    private ServerSocket initSocket() {
        try {
            ServerSocket socket = new ServerSocket(1234);
            return socket;
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("ERR CP_init Не удалось инициализировать сокет");
        }
        return null;
    }

    public ConnectionsProcessor() {
        socket = initSocket();
        users = new ArrayList<>();
    }

    @Override
    public void run() {
        while (true) {
            try {
                System.out.println("ИНФО: Начинаю поиск клиентов");
                Socket client = socket.accept();
                System.out.println("ИНФО: Подключился клиент");
                ClientThread cl = new ClientThread(client);
                users.add(cl);
                cl.start();
            } catch (IOException e) {
                e.printStackTrace();
                System.out.println("ERR CP_run Не удалось принять клиента");
            }
        }
    }

    public static void sendAll(String string) {
        for (ClientThread user: users) {
            user.send(string);
        }
    }

    public static String who() {
        String result = "";
        for (ClientThread user : users) {
            result += user.getNick() + "\n";
        }
        return result;
    }
}
