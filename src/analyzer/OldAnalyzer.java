package analyzer;
/*
import command.Phase;
import console.Console;
import main.JCL;
import command.Command;
import telnet.ClientThread;
import telnet.ConnectionsProcesser;

import java.lang.reflect.Constructor;

public class Analyzer {
    private Parser parser;
    private boolean isCmds;
    private Command c1;
    private Command c2 = null;
    private boolean opIsAnd;
    private boolean backMode;
    private String errorLog = "";

    public String getErrorLog() {
        return errorLog;
    }

    public Analyzer() {
        parser = JCL.getParser();
    }

    public boolean analyze(String str, ClientThread client) {
        errorLog = "";
        if (str.compareTo("")==0) {
            errorLog = "Пустая строка ";
            return false;
        }
        if(getCmdName(str).compareToIgnoreCase("write") == 0) {
            ConnectionsProcesser.sendAll(client.getNick()+" " + str.substring(6));
            return false;
        }
        if(getCmdName(str).compareToIgnoreCase("who") == 0) {
            client.send(ConnectionsProcesser.who());
            return false;
        }
        isCmds = isCmds(str);
        if(isCmds) processCmds(str);
        else processCmd(str);
        if(errorLog.compareTo("") != 0) return false;
        return true;
    }

    public void exec(Console console, ClientThread client) {
        if(errorLog.compareTo("") == 0) {
            if (isCmds) startCmds(console,client);
            else startCmd(console,client);
        }
    }

    private boolean isCmds(String cmd) {
        if (cmd.contains("&&")||cmd.contains("||")) {
            return true;
        }
        else {
            return false;
        }
    }


    private void processCmds(String cmds) {
        cmds = cmds.strip();
        String[] cmdParts = cmds.split(" ");
        //Первое слово это имя команды ?
        if(!parser.isCmdName(cmdParts[0])) {
            errorLog = cmdParts[0] + " - не является именем команды";
            return;
        }
        String op = cmds.contains("&&") ? "&&" : "||";
        if(op.compareTo("&&") == 0) opIsAnd = true;
        else opIsAnd = false;
        String cmd = "";
        int indexOp = 1;
        for(int i = 0; i < cmdParts.length; i++) {
            String arg = cmdParts[i];
            if(arg.compareTo(op) != 0 ) cmd += arg + " ";
            else {
                indexOp = i;
                break;
            }
        }
//        System.out.println("ОТЛАДКА собрана команда - " + cmd);
        c1 = getCommand(cmd);
//        System.out.println("ОТЛАДКА Команда 1 - " + cmd);
        cmd = "";
        backMode = false;
        for(int i = indexOp + 1; i < cmdParts.length; i++) {
            if(cmdParts[i].compareTo("&") != 0) cmd += cmdParts[i] + " ";
            else {
                backMode = true;
            }
        }
        c2 = getCommand(cmd);
    }

    /**Анализирует команду, находит в ней имя команды. Если у команды должны быть аргументы ищет их
     *
   /
    private Command getCommand(String cmd) {
        String cmdParts[] = cmd.split("[ ]");
        if (!parser.isCmdName(cmdParts[0])) {
            return null;
        }
        Class c = parser.getClass(cmdParts[0]);
        Command command = getCommand(c);
        if (command.getNumArgs() == 0) return command;
        if (cmdParts.length == 1) {
            errorLog += "Недостаточно аргументов";
            return null;
        }
        for (int i = 1; i < cmdParts.length; i++)
            command.addArg(cmdParts[i]);
        return command;
    }

    /**Метод возвращает комманду данного класса используя конструктор c main
     *в случае неудачи вернется null
     * @param c класс, объект которого нужно получить
     * @return Команда
    /
    private Command getCommand(Class c) {
        Constructor constructor;
        try {
            constructor = c.getDeclaredConstructor();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
            errorLog += "Analizer/getCommand Не удалось получить стандартный конструктор";
            return null;
        }
        Object o;
        try {
            o = constructor.newInstance();
        } catch (Exception e) {
            e.printStackTrace();
            errorLog += "Analizer/getCommand Не удалось получить экземпляр команды";
            return null;
        }
        return (Command) o;

    }

    /**Метод возврающий имя операций из команды
     * @param cmd Строка содержащая полную команду типа имяКом арг1 арг2 ... аргN
     * @return имяКом /
    private String getCmdName(String cmd) {
        cmd = cmd.strip();
        String[] cmdParts = cmd.split("[ ]");
        return cmdParts[0];
    }


    private void processCmd(String cmd) {
        String cmdName = this.getCmdName(cmd);
//        System.out.println("ОТЛАДКА Имя команды - " + cmdName);
        cmd = cmd.strip();
        String[] cmdParts = cmd.split(" ");
        cmd = "";
        backMode = false;
        for (int i = 0; i < cmdParts.length; i++) {
            if (cmdParts[i].compareTo("&") != 0 || i != cmdParts.length-1) {
                cmd += cmdParts[i] +" ";
            } else {
                backMode = true;
            }
        }
//        System.out.println("ОТЛАДКА состояние команды - " + cmd + " flag - " + backgroundMode);
        c1 = getCommand(cmd);
        if(c1 == null) errorLog = "First command is null";

    }


    private void startCmds(Console console,ClientThread client) {
        if(c1 == null) {
            errorLog = "Ошибка в первой команде";
            if (c2 == null) {
                errorLog = "Ошибка во второй команде";
                return;
            }
            if(!opIsAnd) {
                if (!backMode) {
                    if(client == null) c2.exec(console);
                    else c2.exec(console,client);
                }
                else {
                    if (client == null) c2.exec(console);
                    else c2.exec(console,client);
                }
            }
            return;
        }
        if (c2 == null) {
            errorLog = "Ошибка во второй команде";
            return;
        }
        if(client == null) c1.exec(console);
        else c1.exec(console,client);
        System.out.println(c1.getOut());
        while (c1.getPhase() == Phase.Process) {}
        if(c1.getPhase() == Phase.Error && opIsAnd) {
            System.out.println("Первая комманда прервана, вторая не выполняется!");
        }
        else if(c1.getPhase() == Phase.End && opIsAnd) {
            System.out.println("Первая операция выполнена успешно, вторая выполняется");
            if (!backMode) {

                if (client == null )c2.exec(console);
                else c2.exec(console,client);
            }
            else {
                if (client == null )c2.execBackMode(console);
                else c2.execBackMode(console,client);
            }
        }
        else if (c1.getPhase() == Phase.Error && !opIsAnd) {
            System.out.println("Первая комманда прервана, вторая выполняется!");
            if (!backMode) {
                if (client == null ) c2.exec(console);
                else c2.exec(console,client);
                System.out.println(c2.getOut());
            }
            else {
                if (client == null )c2.execBackMode(console);
                else c2.execBackMode(console,client);
            }
            while (c2.getPhase() == Phase.Process) {}
        }
        else if (c1.getPhase() == Phase.End && !opIsAnd ) {
            System.out.println("Первая операция выполнена успешно, вторая не выполняется");
        }
    }

    private void startCmd(Console console, ClientThread client) {
        if (!backMode) {
            if(client == null) c1.exec(console);
            else c1.exec(console,client);
            System.out.println(c1.getOut());
        }
        else {
            if(client == null) c1.execBackMode(console);
            else c1.execBackMode(console,client);
            System.out.println(c1.getOut());
        }
    }
}
        */