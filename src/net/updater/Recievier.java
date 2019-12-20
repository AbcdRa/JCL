package net.updater;

import analyzer.Analyzer;
import basic.PSConnector;
import net.ClientThread;

import java.io.*;
import java.net.Socket;

//Будет получать сообщения с сокета и выполнять их
public class Recievier {
    private Socket socket;
    private PSConnector console;
    private BufferedReader inBR = null;
    private ClientThread client;
    private boolean isEnd = false;


    public Recievier(ClientThread client) {
        this.client = client;
        console = client.getPSConnector();
        socket = client.getSocket();
        inBR = client.getInBR();
    }


    public void run() {
        while (true) {
            System.out.println("ОТЛАДКА: Жду строку с клиента");
            String line = null;
            try {
                line = inBR.readLine();
                if(line.compareTo("exit") == 0) {
                    return;
                }
            } catch (IOException e) {
                e.printStackTrace();
                return;
            }
            System.out.println("ОТЛАДКА: Строка получена " + line);
            Analyzer.analyze(line, client);
        }
    }
}
