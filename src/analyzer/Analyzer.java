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




}
