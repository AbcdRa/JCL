package command;

import basic.PSConnector;
import net.ClientThread;

public interface Command  {
    void addArg(String arg);
    void exec(PSConnector console, ClientThread client);  //- Выполнить команду в консоли, отправлять логи Клиенту
    String getRawCmd();
    void and(Command command);
    void or(Command command);
    void setBackMode(boolean backMode);
}
