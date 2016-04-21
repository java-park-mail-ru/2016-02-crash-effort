package main;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

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
}
