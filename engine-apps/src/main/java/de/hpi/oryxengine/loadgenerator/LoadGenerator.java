package de.hpi.oryxengine.loadgenerator;

import java.io.FileNotFoundException;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.hpi.oryxengine.NoRunningInstancesLoadgeneratorCaller;
import de.hpi.oryxengine.bootstrap.OryxEngine;
import de.hpi.oryxengine.exception.DefinitionNotFoundException;
import de.hpi.oryxengine.exception.IllegalStarteventException;
import de.hpi.oryxengine.factories.process.ProcessDeployer;
import de.hpi.oryxengine.navigator.NavigatorImpl;


/**
 * The Class LoadGenerator. Is used to generate some load and profile it (more or less) Maybe it should be more generic,
 * but we'll see.
 */
public class LoadGenerator {
    
    /** The Constant MEGABYTE. */
    private static final long MEGABYTE = 1024L * 1024L;

    /** The Constant DEFAULT_PROCESS. */
    private static final String DEFAULT_PROCESS = "ExampleProcessDeployer";
    
    /** The Constant DEFAULT_NUMBER_OF_RUNS. */
    private static final int DEFAULT_NUMBER_OF_RUNS = 1000000;
    
    /** The Constant DEFAULT_NUMBER_OF_THREADS. */
    private static final int DEFAULT_NUMBER_OF_THREADS = 4;
    
    /** The Constant PATH_TO_PROCESS_FACTORIES. */
    private static final String PATH_TO_PROCESS_FACTORIES = "de.hpi.oryxengine.factories.process.";

    /** The logger. */
    private final Logger logger = LoggerFactory.getLogger(getClass());

    /** The runtime. */
    private Runtime runtime;

    /** The number of runs. */
    private int numberOfRuns;

    /** The start time. */
    private long startTime;

    private int numberOfThreads;

    /** The navigator. */
    private NavigatorImpl navigator;

    /** The class name of the processfactory which creates the process that simulates the load. */
    private String className;
    
    /** the UUID of the definition this process is deploying. */
    private UUID definitionId;
    
    /**
     *  Deploys the selected process and is then further used to cleanup
     * as an example, Human task processes got pseudo humans working on them, we need to stop
     * the pseudo humans.
     */
    private ProcessDeployer deployer;

    /**
     * Instantiates a new load generator.
     * 
     * @param className
     *            the class name of the factory to use in the loadgenerator
     * @param numberOfRuns
     *            the number of seperate processes to start
     * @param numberOfThreads
     *            the number of threads the navigator should use
     */
    public LoadGenerator(String className, int numberOfRuns, int numberOfThreads) {
               
        //Initialize the service
        OryxEngine.startWithConfig("/test.oryxengine.cfg.xml");
        
        this.className = PATH_TO_PROCESS_FACTORIES + className;
        this.numberOfRuns = numberOfRuns;
        this.numberOfThreads = numberOfThreads;
        this.runtime = Runtime.getRuntime();
        

        // definitions should be deployed and the appropriate then should be selected...
        deployProcessDefinition();
    }
    
    /**
     * Convenience constructor using all the defaults.
     */
    public LoadGenerator() {
    	this(DEFAULT_PROCESS, DEFAULT_NUMBER_OF_RUNS, DEFAULT_NUMBER_OF_THREADS);
    }
    
    /**
     * Uses the default values except for the number of runs.
     *
     * @param numberOfRuns the number of runs of the instance.
     */
    public LoadGenerator(int numberOfRuns) {
    	this(DEFAULT_PROCESS, numberOfRuns, DEFAULT_NUMBER_OF_THREADS);
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
     * @param bytes
     *            is the number in bytes which should be converted to MB
     * @return the number of megabytes computed
     */
    public static long bytesToMegabytes(long bytes) {

        return bytes / MEGABYTE;
    }

    /**
     * Calls the Example Process token factory in order to create a new process.
     */
    public void deployProcessDefinition() {

        try {
            this.deployer = (ProcessDeployer) Class.forName(className).newInstance();
            this.definitionId = this.deployer.deploy();
        } catch (InstantiationException e) {
            logger.debug("Loading of class " + className + " failed , the name seems to be wrong.", e);
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            logger.debug("Loading of class " + className + " failed , the name seems to be wrong.", e);
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            logger.debug("Loading of class " + className + " failed , the name seems to be wrong.", e);
            e.printStackTrace();
        } catch (IllegalStarteventException e) {
            logger.debug("Loading of class " + className + " failed , start node was wrong.", e);
            e.printStackTrace();
        }

    }

    /**
     * Log memory used - it is calculated inaccurately from the VMs heapspace.
     * 
     * @param messageString
     *            the message string which is logged
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
        this.logger.info("We start to put " + String.valueOf(numberOfRuns) + "  instances from the Factory "
            + className + " into our navigator!");
      //  navigator = (NavigatorImpl) ServiceFactory.getNavigatorService(); 
        navigator = new NavigatorImpl(numberOfThreads);
        NoRunningInstancesLoadgeneratorCaller listener = new NoRunningInstancesLoadgeneratorCaller(this);
        navigator.registerPlugin(listener);
        
        // start the number of process Instances with our process definition
        for (int i = 0; i < this.numberOfRuns; i++) {
            
            try {
                navigator.startProcessInstance(definitionId);
            } catch (DefinitionNotFoundException e) {
                logger.error("Exception not found when starting up our many many instances in the Loadgenerator", e);
            }
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
    public void navigatorCurrentlyFinished() {

        long stopTime = System.currentTimeMillis();
        long runTime = stopTime - this.startTime;
        this.logger.info("Run time for all our " + String.valueOf(this.numberOfRuns) + " instances: " + runTime + "ms");
        this.logMemoryUsed("Used memory in megabytes (before gc run): ");
        this.runtime.gc();
        // Calculate the used memory (in bytes)
        this.logMemoryUsed("Used memory in megabytes (after gc run): ");
        
        this.navigator.stop();
        this.deployer.stop();

    }

    /**
     * gimme some load! No seriously it creates some instances which are run.
     * 
     * @param args
     *            the arguments
     * @throws FileNotFoundException
     *             if the file is not found on HDD
     */
    public static void main(String[] args)
    throws FileNotFoundException {

        LoadGenerator gene = new LoadGenerator();
        gene.execute();
    }
}
