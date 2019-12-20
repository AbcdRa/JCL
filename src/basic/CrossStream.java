package basic;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class CrossStream extends Thread{
    private InputStream in1;
    private OutputStream out1;
    private InputStream in2;
    private OutputStream out2;

    public boolean isInterrupt() {
        return isInterrupt;
    }

    private boolean isInterrupt = false;

    public CrossStream(InputStream in1, OutputStream out1, InputStream in2,  OutputStream out2) {
        this.in1 = in1;
        this.in2 = in2;
        this.out1 = out1;
        this.out2 = out2;
    }

    @Override
    public void run() {
        Runnable redirect1 = () -> {while(true) {
                try {
                    out1.write(in2.readNBytes(in2.available()));
                    out1.flush();
                } catch (IOException e) {
                    e.printStackTrace();
                    break;
                }
            }
            isInterrupt = true;
        };
        Runnable redirect2 = () -> {
            while (true) {
                try {
                    String decode = new String(in1.readNBytes(in1.available()));
                    if(decode.equals("interrupt\n")) {
                        isInterrupt = true;
                        return;
                    }
                    out2.write(decode.getBytes());
                    out2.flush();
                } catch (IOException e) {
                    e.printStackTrace();
                    isInterrupt = true;
                    break;
                }
            }
            isInterrupt = true;

        };
        Thread th1 = new Thread(redirect1);
        Thread th2 = new Thread(redirect2);
        th1.start();
        th2.start();
        try{
            th2.join();
        }
        catch (InterruptedException e) {
            e.printStackTrace();
        }

    }
}
