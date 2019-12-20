package basic;

import command.Command;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

public class StaticFunc {
    static String cutStringTo(InputStream in, String stopStr)  {
        String result = "";
        try {
            byte[] stopBytes = stopStr.getBytes();

            while (true) {
                int currInt = in.read();
                if (currInt == -1) break;
                result += (char) currInt;
                //System.out.println((char)currInt + "  " + currInt);
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
    public  static Object getObjFromClass(Class clazz) {
        Constructor constructor;
        try {
            constructor = clazz.getConstructor();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
            System.out.println("ERR *Func_getObjFromClass Не найден стандартный конструктор");
            return null;
        }
        Object object;
        try {
            object = constructor.newInstance();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
            System.out.println("ERR *Func_getObjFromClass Не имеем доступа к конструктору");
            return null;
        } catch (InstantiationException e) {
            e.printStackTrace();
            System.out.println("ERR *Func_getObjFromClass СВ Не имеем стандартного конструктора");
            return null;
        } catch (InvocationTargetException e) {
            e.printStackTrace();
            System.out.println("ERR *Func_getObjFromClass Нельзя вызвать конструктор");
            return null;
        }
        return object;
    }
}
