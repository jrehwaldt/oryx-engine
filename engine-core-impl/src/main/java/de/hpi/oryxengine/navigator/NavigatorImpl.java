package de.hpi.oryxengine.navigator;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.annotation.Nonnull;

import de.hpi.oryxengine.Service;
import de.hpi.oryxengine.ServiceFactory;
import de.hpi.oryxengine.correlation.registration.StartEvent;
import de.hpi.oryxengine.exception.DefinitionNotFoundException;
import de.hpi.oryxengine.navigator.schedule.FIFOScheduler;
import de.hpi.oryxengine.plugin.AbstractPluggable;
import de.hpi.oryxengine.plugin.navigator.AbstractNavigatorListener;
import de.hpi.oryxengine.process.definition.ProcessDefinition;
import de.hpi.oryxengine.process.instance.ProcessInstance;
import de.hpi.oryxengine.process.instance.ProcessInstanceImpl;
import de.hpi.oryxengine.process.structure.Node;
import de.hpi.oryxengine.process.token.Token;
import de.hpi.oryxengine.repository.ProcessRepository;

/**
 * The Class NavigatorImpl. Our Implementation of the Navigator.
 */
public class NavigatorImpl extends AbstractPluggable<AbstractNavigatorListener> implements Navigator, Service {

    private FIFOScheduler scheduler;

    private List<Token> suspendedTokens;

    /** The execution threads. Yes our navigator is multi-threaded. Pretty awesome. */
    private ArrayList<NavigationThread> executionThreads;

    private static final int NUMBER_OF_NAVIGATOR_THREADS = 10;

    private int navigatorThreads;

    private NavigatorState state;

    private int counter;

    private ProcessRepository repository;

    private List<ProcessInstance> runningInstances;
    private List<ProcessInstance> finishedInstances;

    /**
     * Instantiates a new navigator implementation.
     */
    public NavigatorImpl() {

        this(null, NUMBER_OF_NAVIGATOR_THREADS);
    }

    public NavigatorImpl(int numberOfThreads) {

        this(null, numberOfThreads);
    }

    /**
     * Instantiates a new navigator implementation.
     * 
     * @param numberOfThreads
     *            the number of navigator threads
     */
    public NavigatorImpl(ProcessRepository repo, int numberOfThreads) {

        // TODO Lazy initialized, o rly?
        this.scheduler = new FIFOScheduler();
        this.executionThreads = new ArrayList<NavigationThread>();
        this.state = NavigatorState.INIT;
        this.counter = 0;
        this.navigatorThreads = numberOfThreads;
        //this.repository = ServiceFactory.getRepositoryService();
        this.repository = repo;
        this.suspendedTokens = new ArrayList<Token>();
        this.runningInstances = new ArrayList<ProcessInstance>();
        this.finishedInstances = new ArrayList<ProcessInstance>();
    }

    /**
     * Start. Starts the number of worker thread specified in the NUMBER_OF_NAVIGATOR_THREADS Constant and adds them to
     * the execution threads list.
     */
    @Override
    public void start() {

        // "Gentlemen, start your engines"
        for (int i = 0; i < navigatorThreads; i++) {
            addThread();
        }

        changeState(NavigatorState.RUNNING);
    }

    /**
     * Adds another thread of execution to the navigator.
     */
    public void addThread() {

        NavigationThread thread = new NavigationThread(String.format("NT %d", counter), scheduler);
        thread.start();
        executionThreads.add(thread);
        counter++;
    }

    @Override
    public void startProcessInstance(UUID processID)
    throws DefinitionNotFoundException {

        // TODO use the variable repository here. This cannot be used in tests, as it requires the bootstrap to have run
        // first, but we definitely do not want to start the whole engine to test a simple feature.
        ProcessDefinition definition = ServiceFactory.getRepositoryService().getDefinition(processID);

        ProcessInstance instance = new ProcessInstanceImpl(definition);

        for (Node node : definition.getStartNodes()) {
            Token newToken = instance.createToken(node, this);
            startArbitraryInstance(newToken);
        }
        runningInstances.add(instance);
    }

    @Override
    public void startProcessInstance(UUID processID, StartEvent event)
    throws DefinitionNotFoundException {

        ProcessDefinition definition = repository.getDefinition(processID);

        ProcessInstance instance = new ProcessInstanceImpl(definition);
        Node startNode = definition.getStartTriggers().get(event);
        Token newToken = instance.createToken(startNode, this);
        startArbitraryInstance(newToken);
        runningInstances.add(instance);

        // TODO we need a method that allows the starting on a list of nodes.
    }

    // this method is for first testing only, as we do not have ProcessDefinitions yet
    // TODO make this method private as soon as it is not used anymore
    /**
     * Start arbitrary instance.
     * 
     * @param token
     *            the token
     */
    public void startArbitraryInstance(Token token) {

        this.scheduler.submit(token);
    }

    /**
     * Stop the execution of a processinstance.
     * 
     * @param instanceID
     *            the instance id
     * @see de.hpi.oryxengine.navigator.Navigator#stopProcessInstance(java.lang.String)
     */
    public void stopProcessInstance(UUID instanceID) {

        // TODO do some more stuff if instance doesnt exist and in genereal
        // runningInstances.remove(instanceID);
        // remove from queue...
    }

    /**
     * Get the state of the currently running instance.
     * 
     * @param instanceID
     *            the instance id
     * @return the current instance state
     * @see de.hpi.oryxengine.navigator.Navigator#getCurrentInstanceState(java.lang.String)
     */
    public String getCurrentInstanceState(UUID instanceID) {

        // TODO get the current instance state
        return null;
    }

    /**
     * Stop the Navigator. So in fact you need to stop all the Navigationthreads.
     */
    public void stop() {

        for (NavigationThread executionThread : executionThreads) {
            executionThread.setShouldStop(true);
        }
        changeState(NavigatorState.STOPPED);
    }

    /**
     * Checks if the navigator is idle. That is when there are no process instances in the to navigate list.
     * 
     * @return true, if it is idle
     */
    // Maybe it should be synchronized? Do we care about dirty reads?
    // Lets get dirrrty!
    public boolean isIdle() {

        return this.scheduler.isEmpty();
    }

    /**
     * Changes the state of the navigator.
     * 
     * @param state
     *            the new state
     */
    private void changeState(@Nonnull NavigatorState state) {

        this.state = state;
        setChanged();
        notifyObservers(this.state);
    }

    /**
     * To string.
     * 
     * @return the string {@inheritDoc}
     */
    @Override
    public String toString() {

        return String.format("Navigator [state=%s]", this.state);
    }

    /**
     * Gets the scheduler. TEMPORARY
     * 
     * @return the scheduler
     */
    public FIFOScheduler getScheduler() {

        return scheduler;
    }

    @Override
    public void addWorkToken(Token t) {

        scheduler.submit(t);

    }

    @Override
    public void addSuspendToken(Token t) {

        suspendedTokens.add(t);
    }

    @Override
    public void removeSuspendToken(Token t) {

        suspendedTokens.remove(t);

    }

    @Override
    public List<ProcessInstance> getRunningInstances() {

        return runningInstances;
    }

    @Override
    public List<ProcessInstance> getEndedInstances() {

        return finishedInstances;
    }

    @Override
    public void signalEndedProcessInstance(ProcessInstance instance) {

        runningInstances.remove(instance);

        // TODO maybe throw an exception if the instance provided is not in the running instances list?
        if (runningInstances != null) {
            finishedInstances.add(instance);
        }

    }
}
