package command;

import basic.CrossStream;
import basic.PSConnector;
import net.ClientThread;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;

public class RdrStart implements Command {
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
        rawCmd = "try { " + getRawCmd() + " } catch {echo $error[0]}";
        if(backMode) {
            rawCmd = "sajb -ScriptBlock {" + getRawCmd() + "} ";
        }
        String path = getPath(console);
        console.send(getRawCmd());
        try {
            InputStream in1 = client.getSocket().getInputStream();
            OutputStream out1 = client.getSocket().getOutputStream();
            InputStream in2 = console.getCmdProcess().getInputStream();
            OutputStream out2 = console.getCmdProcess().getOutputStream();
            CrossStream cS = new CrossStream(in1, out1, in2, out2);
            cS.start();
            while (!cS.isInterrupt());
        } catch (IOException e) {
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

    public void joinArgWithCmd() {
        rawCmd = baseCmd + " ";
        for (String arg : args) {
            rawCmd += arg + " ";
        }
    }

    @Override
    public void and(Command command) {

    }

    @Override
    public void or(Command command) {

    }

    @Override
    public void setBackMode(boolean backMode) {
        this.backMode = backMode;
    }
}
