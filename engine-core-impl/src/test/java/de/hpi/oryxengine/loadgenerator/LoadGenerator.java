package de.hpi.oryxengine.loadgenerator;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

public class LoadGenerator {
    private final static String PROPERTIES_FILE_PATH = "src/test/ressources/loadgenerator.properties";
    private Properties properties = new Properties();
    
    private final Logger logger = Logger.getLogger(getClass());
    //private final static Level DEBUG = Level.DEBUG;
    private final static Level ERROR = Level.ERROR;
    
    /**
     * Loads the properties file used to configure the loadgenerator.
     */
    void loadProperties() {
        try {
            properties.load(new FileInputStream(PROPERTIES_FILE_PATH));
        } catch (IOException e) {
            logger.log(ERROR, "Upps we couldn't load the properties file! here is your error " + e.toString());
        }
    }
    
    /**
     * Instantiates a new load generator.
     */
    LoadGenerator() {
        loadProperties();        
    }
    
    public static void main(String args[]) {
        
    }

}
