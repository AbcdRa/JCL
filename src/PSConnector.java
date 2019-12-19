import java.io.IOException;

/**Класс для работы с PS**/
public class PSConnector {

    /**Создаем экземпляр PS**/
    public PSConnector() {
        try {
            Process PS = (new ProcessBuilder("cmd")).inheritIO().start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
