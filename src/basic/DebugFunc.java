package basic;

import command.Command;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

public class DebugFunc {
    public static void displayByteOut(InputStream in) {
        byte[] bytes = null;
        try {
            bytes = in.readNBytes(in.available());
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("Длина - " + bytes.length);
        System.out.println("Строка - " + new String(bytes));
        int i = 0;
        System.out.println("Байты - ");
        while (i < bytes.length) {
            System.out.println((char)bytes[i] + "   " + bytes[i]);
            i++;
        }
    }


}
