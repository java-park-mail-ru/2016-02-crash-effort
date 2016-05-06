package main;

import sun.misc.BASE64Decoder;

import java.io.*;

/**
 * Created by vladislav on 21.04.16.
 */
public class FileHelper {

    @SuppressWarnings("OverlyBroadThrowsClause")
    public static String readAllText(String filename) throws IOException {
        try(BufferedReader br = new BufferedReader(new FileReader(filename))) {
            final StringBuilder sb = new StringBuilder();
            String line = br.readLine();

            while (line != null) {
                sb.append(line);
                sb.append(System.lineSeparator());
                line = br.readLine();
            }
            return sb.toString();
        }
    }

    @SuppressWarnings("OverlyBroadThrowsClause")
    public static void base64ToImage(String data, String filename) throws IOException {
        final byte[] btDataFile = new BASE64Decoder().decodeBuffer(data);
        final File of = new File(filename);
        try (FileOutputStream osf = new FileOutputStream(of)) {
            osf.write(btDataFile);
            osf.flush();
        }
    }
}
