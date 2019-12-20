package command;

import basic.PSConnector;
import net.ClientThread;

import java.util.ArrayList;

public class Start extends AbstractCommand {
    private ArrayList<String> args = new ArrayList<>();

    public Start() {
        baseCmd = "start -FilePath";
        rawCmd = "start";
    }

    @Override
    public void addArg(String arg) {
        args.add(arg);
    }

    @Override
    public void exec(PSConnector console, ClientThread client) {
        joinArgWithCmd();
        rawCmd = "try { " + getRawCmd() + " -ErrorAction stop } catch {echo $error[0]}";
        if(backMode) {
            rawCmd = "Start-Job -ScriptBlock {" + getRawCmd() + "} ";
        }
        String path = getPath(console);
        console.send(getRawCmd());
        client.send(console.getOut(path));
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


}
