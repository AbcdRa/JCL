package analyzer;

import basic.DebugFunc;
import basic.StaticFunc;
import command.Command;
import net.ClientThread;

public class Analyzer {
    private static Parser parser = new Parser();
    private static boolean isCmds = false;

    public static boolean analyze(String line, ClientThread client) {
        isCmds(line);
        if(!isCmds) {
            Command command = processCmd(line, client);
            if(command == null) return false;
            command.exec(client.getPSConnector(), client);
            return true;
        } else {
            processCmds(line, client);
        }
        return false;
    }

    private static void isCmds(String line) {
        isCmds = line.contains("&&") || line.contains("||");
    }

    private static Command processCmd(String line, ClientThread client) {
        line = line.strip();
        String[] cmdParts = line.split(" ");
        if(!parser.isCmdName(cmdParts[0])) {
            client.send(cmdParts[0] + "- не является допустимым именем команды");
            return null;
        }
        Command command = (Command)StaticFunc.getObjFromClass(parser.getClass(cmdParts[0]));
        if(command == null) {
            client.send("Ошибка в инициализации класса команды " + cmdParts[0]);
            return null;
        }
        for (int i = 1; i < cmdParts.length - 1; i++) {
            command.addArg(cmdParts[i]);
        }
        String lastArg = cmdParts[cmdParts.length - 1];
        if(lastArg.equals("&")) {
            command.setBackMode(true);
        }else if((cmdParts.length - 1) != 0) {
            command.addArg(lastArg);
        }
        return command;
    }

    private static Command processCmds(String line, ClientThread client) {
        String[] cmdParts = line.split(" ");
        String line1 = "";
        String line2 = "";
        int i;
        for(i = 0; !cmdParts[i].equals("&&") && !cmdParts[i].equals("||"); i++) {
            line1 += cmdParts[i] + " ";
        }
        String op = cmdParts[i];
        for(i=i+1; i < cmdParts.length - 1; i++) {
            line2 += cmdParts[i] + " ";
        }
        Command c1 = processCmd(line1, client);
        String lastArg = cmdParts[cmdParts.length - 1];
        if(lastArg.equals("&")) {
            if(c1 != null) c1.setBackMode(true);
        } else {
            line2 += lastArg;
        }
        if(c1 == null) {
            client.send("Не удалось распознать " + line1 + " как команду");
            if(op.equals("||")) {
                return processCmd(line2, client);
            }
        }
        Command c2 = processCmd(line2, client);
        if(c2 == null) {
            client.send("Не удалось распознать " + line2 + " как команду");
            if(op.equals("&&")) {
                return c1;
            }
        }
        if(op.equals("&&")) {
            c1.and(client,c2);
            return c1;
        } else {
            c1.or(client, c2);
            return c2;
        }

    }


}
