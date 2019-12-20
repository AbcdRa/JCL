package net.updater;

import analyzer.Analyzer;
import basic.PSConnector;
import net.ClientThread;
import net.ConnectionsProcessor;

import java.io.*;
import java.net.Socket;

//Будет получать сообщения с сокета и выполнять их
public class Recievier {
    private Socket socket;
    private PSConnector console;
    private BufferedReader inBR;
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
            String line;
            try {
                line = inBR.readLine();
                if(line.compareTo("exit") == 0) {
                    return;
                }
            } catch (IOException e) {
                e.printStackTrace();
                return;
            }
            if(line.contains("Who")) {
                for(ClientThread user : ConnectionsProcessor.getUsers()) {
                    client.send(user.getNick()+"\n");
                }
            }
            else if (line.contains("Write")) {
                String[] words = line.split(" ");
                if(words[0].equals("Write"))
                {
                    StringBuilder mess = new StringBuilder();
                    for(int i = 1; i < words.length; i++) {
                        mess.append(words[i]);
                    }
                    ConnectionsProcessor.sendAll(mess.toString());
                }
            } else {
                System.out.println("ОТЛАДКА: Строка получена " + line);
                Analyzer.analyze(line, client);
            }
        }
    }
}
