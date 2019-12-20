package command;

import basic.PSConnector;
import net.ClientThread;

public class Dir implements Command {
    private final String baseCmd = "dir";
    private boolean backMode = false;
    private String rawCmd = "dir";
    @Override
    public void addArg(String arg) { }

    @Override
    public void exec(PSConnector console, ClientThread client) {
        rawCmd = baseCmd + " | select-Object Name";
        if(backMode) {
            rawCmd = "Start-Job -ScriptBlock {" + baseCmd + "} ";
        }
        console.send(getRawCmd());
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
