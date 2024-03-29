package basic;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.Charset;

/**Класс для работы с PS**/
public class PSConnector {

    private InputStream in = null;
    private OutputStream out = null;

    public Process getCmdProcess() {
        return cmdProcess;
    }

    private Process cmdProcess = null;

    /**Создаем экземпляр PS**/
    public PSConnector() {
        try {
            cmdProcess = (new ProcessBuilder("powershell")).start();
            in = cmdProcess.getInputStream();
            out = cmdProcess.getOutputStream();
            getOut(">");
            send("chcp 65001");
            System.out.println(getOut(">"));
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("ERR PSConnector_init Не удалось создать процесс для консоли");
        }
    }

    /**Отправляем строку в поток ввода консоли**/
    public void send(String string) {
        try {
            string += '\n';
            out.write(string.getBytes());
            out.flush();
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("ERR PSConnector_send Не удалось отправить сообщение");
        }
    }

    /**Получаем вывод с потока вывода**/
    public String getOut() {
        String out = "Вывода нет";
        try {
            out = new String(in.readNBytes(in.available()));
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("ERR PSConnector_getOut Не удалось считать вывод с консоли");
        }
        return out;
    }

    /**Буфферизировать и ждать вывод до стоп строки**/
    public String getOut(String stopStr) {
        String result = StaticFunc.cutStringTo(in, stopStr);
        try {
            result += "" + new String(cmdProcess.getErrorStream().readNBytes(cmdProcess.getErrorStream().available()));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

}
