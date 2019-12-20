package command;

import basic.PSConnector;
import net.ClientThread;

public class Cd extends AbstractCommand {
    private final String baseCmd = "cd";
    private String rawCmd = "cd";
    private String arg;
    private boolean backMode = false;

    @Override
    public void setBackMode(boolean backMode) {
        this.backMode = backMode;
    }

    @Override
    public void addArg(String arg) {
        this.arg = arg;
    }

    @Override
    public void exec(PSConnector console, ClientThread client) {
        rawCmd = "try { " + baseCmd + " " + arg + " -ErrorAction stop } catch {echo $error[0]}";
        if(backMode) {
            rawCmd = "Start-Job -ScriptBlock {" + rawCmd + "} ";
        }
        console.send(getRawCmd());
        client.send(console.getOut(">"));
    }

    @Override
    public String getRawCmd() {
        return rawCmd;
    }

    @Override
    public String joinArgWithCmd() {
        return baseCmd + " " + arg;
    }




}
