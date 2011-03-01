package de.hpi.oryxengine.loadgenerator;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;
import java.util.UUID;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.hpi.oryxengine.factory.ExampleProcessInstanceFactory;
import de.hpi.oryxengine.navigator.NavigatorImpl;
import de.hpi.oryxengine.process.instance.ProcessInstance;
import de.hpi.oryxengine.process.instance.ProcessInstanceImpl;

/**
 * The Class LoadGenerator. Is used to generate some load and profile it (more or less) Maybe it should be more generic,
 * but we'll see.
 */
public class LoadGenerator {

    /** The Constant PROPERTIES_FILE_PATH. */
    private final static String PROPERTIES_FILE_PATH = "src/test/resources/loadgenerator.properties";

    /** The properties. */
    private Properties properties = new Properties();

    /** The logger. */
    private final Logger logger = LoggerFactory.getLogger(getClass());
    
    /**
     * Gets the properties.
     * 
     * @return the properties
     */
    public Properties getProperties() {

        return properties;
    }

    /**
     * Gets the number of runs.
     * 
     * @return the number of runs
     */
    public int getNumberOfRuns() {

        return numberOfRuns;
    }

    /**
     * Gets the logger.
     * 
     * @return the logger
     */
    public Logger getLogger() {

        return logger;
    }

    /** The number of runs. */
    private int numberOfRuns;

    /**
     * Loads the properties file used to configure the loadgenerator.
     * @throws FileNotFoundException 
     */
    void loadProperties() throws FileNotFoundException {
        FileInputStream f =  new FileInputStream(PROPERTIES_FILE_PATH);
        try {
            properties.load(f);
            numberOfRuns = Integer.parseInt((String) this.properties.get("numberOfInstances"));
        } catch (IOException e) {
            logger.error("Upps we couldn't load the properties file!",  e);
        } finally {
            IOUtils.closeQuietly(f);
        }

    }

    /**
     * Calls the Example Process Instance factory in order to create a new one.
     * 
     * @return the example process instance
     */
    public ProcessInstance getExampleProcessInstance() {

        ExampleProcessInstanceFactory factory = new ExampleProcessInstanceFactory();
        return factory.create();
    }

    /**
     * Instantiates a new load generator.
     * @throws FileNotFoundException 
     */
    LoadGenerator() throws FileNotFoundException {

        loadProperties();
    }

    /**
     * gimme some load! No seriously it creates some instances which are run.
     * 
     * @param args
     *            the arguments
     * @throws FileNotFoundException 
     */
    public static void main(String[] args) throws FileNotFoundException {

        LoadGenerator gene = new LoadGenerator();
        long startTime = System.currentTimeMillis();
        NavigatorImpl navigator = new NavigatorImpl();
        navigator.start();
        gene.getLogger().info("Started the navigator!");

        for (int i = 0; i < gene.getNumberOfRuns(); i++) {
            ProcessInstanceImpl p = (ProcessInstanceImpl) gene.getExampleProcessInstance();
            navigator.startArbitraryInstance(UUID.randomUUID(), p);
            gene.getLogger().info(
                "Started Processinstance " + Integer.toString(i + 1) + " of "
                    + Integer.toString(gene.getNumberOfRuns()));
        }

        // TODO this is just temporary.. we got to get back a hook when the queue is empty
        long stopTime = System.currentTimeMillis();
        long runTime = stopTime - startTime;
        gene.getLogger().info(
            "Run time for all our " + Integer.toString(gene.getNumberOfRuns()) + " instances: " + runTime + "ms");

    }

}
