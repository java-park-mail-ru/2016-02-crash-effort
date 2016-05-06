package threading;

import main.FileHelper;

import java.io.IOException;

/**
 * Created by vladislav on 06.05.16.
 */
public class ThreadConverter extends Thread {

    final String data;
    final String filename;

    public ThreadConverter(String data, String filename) {
        this.data = data;
        this.filename = filename;
    }

    @Override
    public void run() {
        try {
            FileHelper.base64ToImage(data, filename);
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }
}
