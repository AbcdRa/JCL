package command;

import basic.PSConnector;
import net.ClientThread;

import java.util.ArrayList;

public class ScrStart implements Command {
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
        client.send(console.getOut(path));
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
