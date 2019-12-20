package analyzer;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.*;
import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class Parser {
    private final static String FILE_PATH = "jcl.xml";
    private static Document document;
    private static Map<String,String> dict = new HashMap<>();

    public static Map<String,String> getDict() {
        return dict;
    }

    public Parser()
    {
        //Получаем документ
        document = getDocument();
        //Если не получилось найти выводим ошибку
        if (document==null) System.out.println("Не удалось инициализировать документ!");
        //Инициализируем словарь соответствий имя - команда
        initDict();
    }

    private void initDict() {
        Element root = document.getDocumentElement();
        NodeList nodeList = root.getElementsByTagName("Command");
        for (int i = 0; i < nodeList.getLength(); i++) {
            //Получаем значение именованного аттрибута string
            String cmdStr = nodeList.item(i).getAttributes().getNamedItem("string").getNodeValue();
            String nameClass = nodeList.item(i).getAttributes().getNamedItem("class").getNodeValue();
            dict.put(cmdStr,nameClass);
        }
    }

    /**Открываем xml документ*/
    private Document getDocument() {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = null;
        try {
            builder = factory.newDocumentBuilder();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
            System.out.println("ERR Parser_getDocument Неудачная конфигурация парсера");
        }
        Document document = null;
        {
            try {
                document = builder.parse(new File(FILE_PATH));
            } catch (SAXException e) {
                System.out.println("ERR Parser_getDocument SAXException");
                e.printStackTrace();
            } catch (IOException e) {
                System.out.println("ERR Parser_getDocument: Не найден конфигурационный файл jcl.xml");
                e.printStackTrace();
            }
        }
        return document;
    }

    /** Метод возвращает названние класса получаемого с xml файла
     * соответсвующей команде переданной в качестве аргумента.
     * Если класс ненайден возвращает null*/
    private String getClassName(String cmd) {
        //Получаем корневой элемента
        Element root = document.getDocumentElement();
        //Получаем список всех комманд
        NodeList nodeList = root.getElementsByTagName("Command");
        for (int i = 0; i < nodeList.getLength(); i++) {
            //Получаем значение именованного аттрибута string
            String cmdStr = nodeList.item(i).getAttributes().getNamedItem("string").getNodeValue();
            if(cmd.compareToIgnoreCase(cmdStr) == 0)
                return nodeList.item(i).getAttributes().getNamedItem("class").getNodeValue();
        }
        return null;
    }


    /**По имени команды пытается найти соответствующий ей класс
     * и вернуть его. В случае неудачи вернет null
     * @param cmdOp Имя команды
     * @return Класс обрабатывающий эту команду
     */
    public Class getClass(String cmdOp) {
        cmdOp = cmdOp.strip();
        String className = "command." + getClassName(cmdOp);
        if(className == null) {
            System.out.println("Не удалось распознать класс соответствующей комманде" + cmdOp);
            return null;
        }
        else {
            try {
                return Class.forName(className);
            } catch (ClassNotFoundException e) {
                System.out.println("Не удалось найти класс " +className+ " по имени");
                e.printStackTrace();
            }
            return null;
        }
    }

    /**Метод для проверки является ли указанная строка именем операции
     * @param str проверяемая строка
     * @return true : Если это имя команды, false : Не является именем команды
     */
    public boolean isCmdName (String str) {
        if(getClass(str) == null) return false;
        else return true;
    }
}

