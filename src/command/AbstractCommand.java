package command;

import basic.PSConnector;
import net.ClientThread;

public abstract class AbstractCommand implements Command {
    String baseCmd = null;
    String rawCmd = null;
    protected boolean backMode = false;

    public void setBackMode(boolean backMode) {
        this.backMode = backMode;
    }

    public String getRawCmd() {
        return rawCmd;
    }

    public void and(ClientThread client, Command command) {
        PSConnector console = client.getPSConnector();
        rawCmd = "try { " + joinArgWithCmd() + " -ErrorAction stop ; " + command.joinArgWithCmd() + " } catch {echo $error[0]}";
        if(backMode) {
            rawCmd = "Start-Job -ScriptBlock { " + rawCmd + " } ";
        }
        console.send(rawCmd);
        client.send(console.getOut(">"));
    }

    public void or(ClientThread client, Command command) {
        PSConnector console = client.getPSConnector();
        rawCmd = "try { " + joinArgWithCmd() + " -ErrorAction stop } catch { " + command.joinArgWithCmd() + " }";
        if(backMode) {
            rawCmd = "Start-Job -ScriptBlock { " + rawCmd + " } ";
        }
        console.send(rawCmd);
        client.send(console.getOut(">"));
    }
}
