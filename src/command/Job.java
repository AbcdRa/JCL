package command;

import basic.PSConnector;
import net.ClientThread;

public class Job extends AbstractCommand {
    private int arg = -1;

    public Job() {
        baseCmd = "gjb";
        rawCmd = "gjb";
    }

    @Override
    public void addArg(String arg) {
        try {
            this.arg = Integer.parseInt(arg);
        } catch (Exception e ) {
            this.arg = -1;
        }
    }

    @Override
    public void exec(PSConnector console, ClientThread client) {
        rawCmd = baseCmd;
        if(arg != -1) rawCmd = "Receive-Job -Id " + arg;
        if(backMode) {
            rawCmd = "Start-Job -ScriptBlock {" + rawCmd + "} ";
        }
        console.send(rawCmd + "\n");
        client.send(console.getOut(">"));
    }

    @Override
    public String joinArgWithCmd() {
        if(arg == -1) return baseCmd;
        else {return rawCmd = "Receive-Job -Id " + arg;}
    }
}
