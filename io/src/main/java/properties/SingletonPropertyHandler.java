package properties;

import org.apache.log4j.Logger;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

// TODO: 02.01.2017 Replace by multitone for each property file
public class SingletonPropertyHandler {

    private static final String NO_VALUE = "Here should be a description soon";
    private static final Logger logger = Logger.getLogger(SingletonPropertyHandler.class);
    private static final SingletonPropertyHandler instance = new SingletonPropertyHandler();
    private final Map<String, String> map = new HashMap<>();

    public static SingletonPropertyHandler getInstance() {
        return instance;
    }

    private SingletonPropertyHandler() {
        try (InputStream input = SingletonPropertyHandler.class.getResourceAsStream("/properties/app.properties")) {
            final Properties props = new Properties();
            props.load(input);
            props.forEach((k, v) -> map.put((String) k, (String) v));
        } catch (Exception e) {
            logger.warn("Failed to load property file", e);
        }
    }

    public String getValue(String key) {
        String value = null;
        try {
            value = map.get(key);
        } catch (Exception e) {
            logger.warn("Failed to get property", e);
        }
        if (value==null || value.isEmpty()) {
            return NO_VALUE;
        }
        return value;
    }
}
