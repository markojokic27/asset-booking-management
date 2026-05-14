package config;

import lombok.extern.log4j.Log4j2;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.HashMap;
import java.util.Map;

@Log4j2
public class ConfigFromFile {

    protected static final Map<String, String> parameters;

    static {
        parameters = readConfigFile();
    }

    private static Map<String, String> readConfigFile() {
        Map<String, String> attributes = new HashMap<>();
        try (BufferedReader config = new BufferedReader(
                new FileReader("src/main/resources/setup.properties"))) {
            readSystem(config, attributes);
        } catch (Exception e) {
            log.error("readConfigFile", e);
        }
        return attributes;
    }

    private static void readSystem(BufferedReader config, Map<String, String> attributes) {
        try {
            String line;
            String key;
            String value;
            int splitIndex;
            while ((line = config.readLine()) != null) {
                if (!line.isEmpty() && line.charAt(0) != '#') {
                    splitIndex = line.indexOf('=');
                    if (splitIndex > -1) {
                        key = line.substring(0, splitIndex);
                        key = key.trim();
                        value = line.substring(splitIndex + 1);
                        value = value.trim();
                        attributes.put(key, value);
                    }
                }
            }
        } catch (Exception e) {
            log.error("readSystem", e);
        }
    }

    public static Map<String, String> getParameters() {
        return parameters;
    }

}
