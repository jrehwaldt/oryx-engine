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
import de.hpi.oryxengine.plugin.scheduler.SchedulerEmptyListener;
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
    
    private SchedulerEmptyListener listener;
    
    /**
     * Instantiates a new load generator.
     * @throws FileNotFoundException 
     */
    LoadGenerator() throws FileNotFoundException {

        loadProperties();
        this.listener = SchedulerEmptyListener.getInstance(this);
    }
    
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

    private long startTime;

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
     * Execute.
     */
    public void execute() {
        
        this.logger.info("We start to put our instances into our navigator!");
        NavigatorImpl navigator = new NavigatorImpl();
        navigator.getScheduler().registerPlugin(SchedulerEmptyListener.getInstance(this));
        
        for (int i = 0; i < this.getNumberOfRuns(); i++) {
            ProcessInstanceImpl p = (ProcessInstanceImpl) this.getExampleProcessInstance();
            navigator.startArbitraryInstance(UUID.randomUUID(), p);
            /*this.logger.info(
                "Started Processinstance " + Integer.toString(i + 1) + " of "
                    + Integer.toString(this.getNumberOfRuns()));*/
        }
        
        this.logger.info("Finished putting instances into our navigator!");
        this.logger.info("The navigator will be started in a millisecond - take the time!");
        this.startTime = System.currentTimeMillis();
        navigator.start();
        
    }
    
    /**
     * When the scheduler queue is empty, the Load Generator should stop 
     * measuring the time for process instances' execution time.
     */
    public void schedulerIsEmpty() {

        long stopTime = System.currentTimeMillis();
        long runTime = stopTime - this.startTime;
        this.logger.info(
            "Run time for all our " + Integer.toString(this.getNumberOfRuns()) + " instances: " + runTime + "ms");

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
        gene.execute();
    }
}

