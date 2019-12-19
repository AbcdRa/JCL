package basic;

import java.io.IOException;
import java.io.InputStream;

public class StaticFunc {
    public static String cutStringTo(InputStream in, String stopStr)  {
        String result = "";
        try {
            byte[] stopBytes = stopStr.getBytes();

            while (true) {
                int currInt = in.read();
                if (currInt == -1) break;
                result += (char) currInt;
                for (int i = 0; currInt == stopBytes[i]; i++) {
                    currInt = in.read();
                    result += (char) currInt;
                    if (i == stopBytes.length - 1) return result;
                }
            }
        }catch (IOException e) {
            e.printStackTrace();
            System.out.println("ERR StaticFunc_cutStringTo Не удалось считать байт с потока ввода");
        }
        return result;
    }
}
