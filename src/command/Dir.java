package command;

import basic.PSConnector;
import net.ClientThread;

public class Dir extends AbstractCommand{
    public Dir() {
        rawCmd = "dir";
        baseCmd = "dir";
    }


    @Override
    public void addArg(String arg) { }

    @Override
    public String joinArgWithCmd() {
        return baseCmd;
    }

    @Override
    public void exec(PSConnector console, ClientThread client) {
        rawCmd = baseCmd + " | select-Object Name";
        if(backMode) {
            rawCmd = "Start-Job -ScriptBlock {" + baseCmd + "} ";
        }
        console.send(getRawCmd());
        client.send(console.getOut(">"));
    }

}
