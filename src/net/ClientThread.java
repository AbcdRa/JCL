package net;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;

import basic.*;
import net.updater.Recievier;

public class ClientThread extends Thread{
    private Socket socket;
    private PSConnector psConnector;
    private Scanner scanner;
    private BufferedWriter outBW;
    private BufferedReader inBR;
    private String name;
    private Recievier recievier;

    public String getNick() {
        return name;
    }
    public Socket getSocket() {
        return socket;
    }
    public PSConnector getPSConnector() {
        return psConnector;
    }
    public BufferedReader getInBR() {
        return inBR;
    }
    public BufferedWriter getOutBW() {
        return outBW;
    }

    public ClientThread(Socket socket) {
        this.socket = socket;
        scanner = new Scanner(System.in);
        try {
            InputStream inS = socket.getInputStream();
            OutputStream outS = socket.getOutputStream();
            InputStreamReader inSR = new InputStreamReader(inS);
            OutputStreamWriter outSW = new OutputStreamWriter(outS);
            inBR = new BufferedReader(inSR);
            outBW = new BufferedWriter(outSW);
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("ERR ClientThread_init не удалось инициализировать потоки");
        }

    }

    @Override
    public void run() {
        psConnector = new PSConnector();
        try {
            send("ENTER NAME: ");
            name = inBR.readLine();
            System.out.println("DEBUG Имя клиента инициализировано - " + name);
            send("HI, " + name );
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("ERR ClientThread_run не удачная регистрация");
        }
        recievier = new Recievier(this);
        recievier.run();
    }

    public void send(String string) {
        try {
            outBW.write(string+"\n");
            outBW.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
