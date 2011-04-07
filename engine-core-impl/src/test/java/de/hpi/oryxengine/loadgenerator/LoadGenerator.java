package de.hpi.oryxengine.loadgenerator;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.jar.Attributes.Name;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.hpi.oryxengine.OryxEngine;
import de.hpi.oryxengine.ServiceFactory;
import de.hpi.oryxengine.exception.IllegalStarteventException;
import de.hpi.oryxengine.factory.process.AbstractProcessDeployer;
import de.hpi.oryxengine.factory.process.ProcessDeployer;
import de.hpi.oryxengine.navigator.NavigatorImpl;
import de.hpi.oryxengine.plugin.navigator.NavigatorListenerLogger;
import de.hpi.oryxengine.plugin.navigator.NoRunningInstancesLoadgeneratorCaller;
import de.hpi.oryxengine.process.definition.NodeParameter;
import de.hpi.oryxengine.process.definition.ProcessDefinition;
import de.hpi.oryxengine.process.instance.ProcessInstance;
import de.hpi.oryxengine.process.instance.ProcessInstanceImpl;
import de.hpi.oryxengine.process.structure.Node;
import de.hpi.oryxengine.process.token.Token;
import de.hpi.oryxengine.process.token.TokenImpl;

/**
 * The Class LoadGenerator. Is used to generate some load and profile it (more or less) Maybe it should be more generic,
 * but we'll see.
 */
public class LoadGenerator {
    
    /** The Constant MEGABYTE. */
    private static final long MEGABYTE = 1024L * 1024L;

    /** The Constant DEFAULT_PROCESS. */
    // TODO replace via process definitions from the repository
    private static final String DEFAULT_PROCESS = "HeavyComputationProcessDeployer";
    
    /** The Constant DEFAULT_NUMBER_OF_RUNS. */
    private static final int DEFAULT_NUMBER_OF_RUNS = 10;
    
    /** The Constant DEFAULT_NUMBER_OF_THREADS. */
    private static final int DEFAULT_NUMBER_OF_THREADS = 4;
    
    /** The Constant PATH_TO_PROCESS_FACTORIES. */
    // TODO replace with repository
    private static final String PATH_TO_PROCESS_FACTORIES = "de.hpi.oryxengine.factory.process.";

    /** The logger. */
    private final Logger logger = LoggerFactory.getLogger(getClass());

    /** The runtime. */
    private Runtime runtime;

    /** The number of runs. */
    private int numberOfRuns;

    /** The start time. */
    private long startTime;

    /** The number of threads. */
    private int numberOfThreads;

    /** The navigator. */
    private NavigatorImpl navigator;

    /** The class name of the processfactory which creates the process that simulates the load. */
    private String className;

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
        OryxEngine.startWithConfig("test.oryxengine.cfg.xml");
        
        this.className = PATH_TO_PROCESS_FACTORIES + className;
        this.numberOfRuns = numberOfRuns;
        this.numberOfThreads = numberOfThreads;
        this.runtime = Runtime.getRuntime();
        

        // definitions should be deployed and the appropriate then should be selected...
        deployProcessDefinition();
        

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

        ProcessDeployer factory;
        try {
            factory = (ProcessDeployer) Class.forName(className).newInstance();
            factory.deploy();
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

        
        // Lookup the process Definition and get the start tokens
        List<Token> tokenList = new ArrayList<Token>();
        // TODO atm first implementation only with the first process in the repository,
        // this should be later changed to a more generic way
        
        ProcessDefinition definition = ServiceFactory.getRepositoryService().getDefinitions().get(0);
        ProcessInstance instance = new ProcessInstanceImpl(definition);
        for (Node startNode : definition.getStartNodes()) {
            tokenList.add(instance.createToken(startNode, navigator));
        }

        for (int i = 0; i < this.numberOfRuns; i++) {
            // Start the process with all the start tokens
            for (Token token : tokenList) {
                navigator.startArbitraryInstance(token);
            }

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
    
    
    public void navigatorCurrentlyFinished() {

        long stopTime = System.currentTimeMillis();
        long runTime = stopTime - this.startTime;
        this.logger.info("Run time for all our " + String.valueOf(this.numberOfRuns) + " instances: " + runTime + "ms");
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
     * @throws FileNotFoundException
     *             if the file is not found on HDD
     */
    public static void main(String[] args)
    throws FileNotFoundException {

        LoadGenerator gene = new LoadGenerator(DEFAULT_PROCESS, DEFAULT_NUMBER_OF_RUNS, DEFAULT_NUMBER_OF_THREADS);
        gene.execute();
    }
}
