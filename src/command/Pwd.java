package command;

import basic.PSConnector;
import net.ClientThread;

public class Pwd implements Command {
    private final String baseCmd = "pwd";
    private String rawCmd = "pwd";
    private boolean backMode = false;
    @Override
    public void addArg(String arg) { }

    @Override
    public void exec(PSConnector console, ClientThread client) {
        if(backMode) {
            rawCmd = "Start-Job -ScriptBlock {" + rawCmd + "} ";
        }
        console.send(rawCmd);
        client.send(console.getOut(">"));
    }

    @Override
    public String getRawCmd() {
        return rawCmd;
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
