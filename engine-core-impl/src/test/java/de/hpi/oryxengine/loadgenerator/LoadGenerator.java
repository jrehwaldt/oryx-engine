package de.hpi.oryxengine.loadgenerator;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.hpi.oryxengine.factory.HeavyComputationProcessTokenFactory;
import de.hpi.oryxengine.navigator.NavigatorImpl;
import de.hpi.oryxengine.plugin.scheduler.SchedulerEmptyListener;
import de.hpi.oryxengine.process.token.Token;
import de.hpi.oryxengine.process.token.TokenImpl;

/**
 * The Class LoadGenerator. Is used to generate some load and profile it (more or less) Maybe it should be more generic,
 * but we'll see.
 */
public class LoadGenerator {

    /** The Constant PROPERTIES_FILE_PATH. */
    private final static String PROPERTIES_FILE_PATH = "/loadgenerator.properties";
    
    private static final long MEGABYTE = 1024L * 1024L;

    /** The properties. */
    private Properties properties = new Properties();

    /** The logger. */
    private final Logger logger = LoggerFactory.getLogger(getClass());
    
    private Runtime runtime;
    
    private int numberOfRuns;

    private long startTime;

    private int numberOfThreads;

    private NavigatorImpl navigator;
    
    private String className;

    /**
     * Instantiates a new load generator.
     *
     * @throws FileNotFoundException if there is no such properties file to be loaded
     */
    public LoadGenerator()
    throws FileNotFoundException {

        loadProperties();
        this.runtime = Runtime.getRuntime();

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

    /**
     * Computes megabytes from bytes.
     * 
     * @param bytes is the number in bytes which should be converted to MB
     * @return the number of megabytes computed
     */
    public static long bytesToMegabytes(long bytes) {
        return bytes / MEGABYTE;
    }

    /**
     * Loads the properties file used to configure the load generator.
     * 
     * @throws FileNotFoundException if the file is not found on the HDD
     */
    void loadProperties()
    throws FileNotFoundException {

        // FileInputStream f = new FileInputStream(PROPERTIES_FILE_PATH);
        try {
            properties.load(LoadGenerator.class.getResourceAsStream(PROPERTIES_FILE_PATH));
            numberOfRuns = Integer.parseInt((String) this.properties.get("numberOfInstances"));
            numberOfThreads = Integer.parseInt((String) this.properties.get("numberOfThreads"));
            className = (String) this.properties.getProperty("processMoped"); 
        } catch (IOException e) {
            logger.error("Upps we couldn't load the properties file!", e);
        }

    }

    /**
     * Calls the Example Process token factory in order to create a new one.
     * 
     * @return the example process token
     */
    public Token getExampleProcessToken() {

            
        HeavyComputationProcessTokenFactory factory = new HeavyComputationProcessTokenFactory();
            return factory.create();
    }
    
    /**
     * Log memory used - it is calculated inaccurately from the VMs heapspace.
     *
     * @param messageString the message string which is logged
     */
    private void logMemoryUsed(String messageString) {
        long memory = runtime.totalMemory() - runtime.freeMemory();
        this.logger.info(messageString + bytesToMegabytes(memory));
    }
    
    /**
     * Execute.
     */
    public void execute() {

        // cleanup before we get started
        this.runtime.gc();
        // Calculate the used memory (in bytes)
        this.logMemoryUsed("Used memory in megabytes at the very beginning: ");
        this.logger.info("We start to put our instances into our navigator!");
        navigator = new NavigatorImpl(numberOfThreads);
        navigator.getScheduler().registerPlugin(SchedulerEmptyListener.getToken(this));

        for (int i = 0; i < this.getNumberOfRuns(); i++) {
            TokenImpl p = (TokenImpl) this.getExampleProcessToken();
            navigator.startArbitraryInstance(UUID.randomUUID(), p);
            /*
             * this.logger.info( "Started Process token " + Integer.toString(i + 1) + " of " +
             * Integer.toString(this.getNumberOfRuns()));
             */
        }

        this.logger.info("Finished putting instances into our navigator!");
        this.logMemoryUsed("Memory used after we put all the instances in our navigator:");
        this.logger.info("The navigator will be started in a millisecond - take the time!");
        this.startTime = System.currentTimeMillis();
        navigator.start();

    }

    /**
     * When the scheduler queue is empty, the Load Generator should stop measuring the time for process instances'
     * execution time.
     */
    public void schedulerIsEmpty() {

        navigator.stop();
        long stopTime = System.currentTimeMillis();
        long runTime = stopTime - this.startTime;
        this.logger.info("Run time for all our " + Integer.toString(this.getNumberOfRuns()) + " instances: " + runTime
            + "ms");
        this.logMemoryUsed("Used memory in megabytes (before gc run): ");
        this.runtime.gc();
        // Calculate the used memory (in bytes)
        this.logMemoryUsed("Used memory in megabytes (after gc run): ");


    }

    /**
     * gimme some load! No seriously it creates some instances which are run.
     * 
     * @param args
     *            the arguments
     * @throws FileNotFoundException if the file is not found on HDD
     */
    public static void main(String[] args)
    throws FileNotFoundException {

        LoadGenerator gene = new LoadGenerator();
        gene.execute();
    }
}
