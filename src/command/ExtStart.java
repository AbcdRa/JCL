package command;

import basic.CrossStream;
import basic.PSConnector;
import net.ClientThread;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;

public class ExtStart implements Command {
    private ArrayList<String> args = new ArrayList<>();
    private String baseCmd = "";
    private String rawCmd = "";
    private Boolean backMode = false;

    @Override
    public void addArg(String arg) {
        args.add(arg);
    }

    @Override
    public void exec(PSConnector console, ClientThread client) {
        joinArgWithCmd();
        try {
            Process process = (new ProcessBuilder(args)).start();
            InputStream in1 = client.getSocket().getInputStream();
            OutputStream out1 = client.getSocket().getOutputStream();
            InputStream in2 = process.getInputStream();
            OutputStream out2 = process.getOutputStream();
            InputStream err = process.getErrorStream();
            new CrossStream(in1, out1,in2, out2, err).start();
            process.waitFor();
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }


    }

    @Override
    public String getRawCmd() {
        return rawCmd;
    }


    private String getPath(PSConnector console) {
        console.send("$p = pwd | select-Object Path");
        console.getOut(">");
        console.send("$p.Path ;");
        console.getOut(";");
        String path = console.getOut("\r");
        path = path.substring(0, path.length() - 2);
        console.getOut(">");
        return path;
    }

    public String joinArgWithCmd() {
        StringBuilder sb = new StringBuilder();
        sb.append(baseCmd);
        sb.append(" ");
        for (String arg : args) {
            sb.append(arg);
            sb.append(" ");
        }
        rawCmd = sb.toString();
        return rawCmd;
    }

    public void and(ClientThread client, Command command) {
        //Процессы нельзя связать с другими коммандами
    }

    public void or(ClientThread client, Command command) {
        //Процессы нельзя связать с другими коммандами
    }

    @Override
    public void setBackMode(boolean backMode) {
        //Процесс перенаправляющий потоки нельзя запустить в фоновом режиме
    }
}
