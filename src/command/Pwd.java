package command;

import basic.PSConnector;
import net.ClientThread;

public class Pwd extends AbstractCommand {

    public Pwd() {
        rawCmd = "pwd";
        baseCmd = "pwd";
    }
    @Override
    public void addArg(String arg) { }

    @Override
    public String joinArgWithCmd() {
        return baseCmd;
    }

    @Override
    public void exec(PSConnector console, ClientThread client) {
        if(backMode) {
            rawCmd = "Start-Job -ScriptBlock {" + rawCmd + "} ";
        }
        console.send(rawCmd);
        client.send(console.getOut(">"));
    }


}
