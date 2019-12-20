import java.io.Console;
import java.nio.charset.Charset;
import java.util.Scanner;

import analyzer.Parser;
import basic.*;
import net.ConnectionsProcessor;

public class JCL {
    public static void main(String[] args) throws Exception{
        new Thread(new ConnectionsProcessor()).start();
    }
}
