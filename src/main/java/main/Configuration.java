package main;

import javax.ws.rs.NotFoundException;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

/**
 * Created by vladislav on 21.04.16.
 */
public class Configuration {

    private final Properties properties;

    @SuppressWarnings("OverlyBroadThrowsClause")
    public Configuration(String filename) throws IOException {
        properties = new Properties();
        try (final FileInputStream fis = new FileInputStream(filename)) {
            properties.load(fis);
        }
    }

    public String getString(String key) throws NotFoundException {
        if (!properties.containsKey(key))
            throw new NotFoundException("Property not found: " + key);
        return properties.getProperty(key);
    }

    public int getInt(String key) throws NotFoundException, NumberFormatException {
        if (!properties.containsKey(key))
            throw new NotFoundException("Property not found: " + key);
        return Integer.valueOf(properties.getProperty(key));
    }
}
