package de.hpi.oryxengine.navigator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import javax.annotation.Nonnull;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.hpi.oryxengine.RepositoryService;
import de.hpi.oryxengine.Service;
import de.hpi.oryxengine.ServiceFactory;
import de.hpi.oryxengine.correlation.registration.StartEvent;
import de.hpi.oryxengine.exception.DefinitionNotFoundException;
import de.hpi.oryxengine.navigator.schedule.FIFOScheduler;
import de.hpi.oryxengine.navigator.schedule.Scheduler;
import de.hpi.oryxengine.plugin.AbstractPluggable;
import de.hpi.oryxengine.plugin.navigator.AbstractNavigatorListener;
import de.hpi.oryxengine.process.definition.ProcessDefinition;
import de.hpi.oryxengine.process.instance.ProcessInstance;
import de.hpi.oryxengine.process.instance.ProcessInstanceImpl;
import de.hpi.oryxengine.process.structure.Node;
import de.hpi.oryxengine.process.token.Token;

/**
 * The Class NavigatorImpl. Our Implementation of the Navigator.
 */
public class NavigatorImpl extends AbstractPluggable<AbstractNavigatorListener> implements Navigator, Service {

    /**
     * Holds all the process tokens that are ready to be executed. Also implements some kind of scheduling algorithm.
     * (Tokens are the uni in which we schedule)
     */
    private Scheduler scheduler;

    /**
     * All the tokens that are suspended for some reason, for example because of a human task.
     */
    private List<Token> suspendedTokens;

    /**
     * All the process Instances (not tokens!) that are currently running for some reason.
     */
    private List<ProcessInstance> runningInstances;

    /**
     * All the process instances that finished i.e. that reached all their end events. Temporary until we can save them,
     * the way they are implemented now, they eat up a huge amount of heap space, as they are the last reference to
     * stuff the garbage collector would normally eat.
     */
    private List<ProcessInstance> finishedInstances;

    /** The execution threads. Yes our navigator is multi-threaded. Pretty awesome. */
    private ArrayList<NavigationThread> executionThreads;

    private static final int NUMBER_OF_NAVIGATOR_THREADS = 10;

    private int navigatorThreads;

    private NavigatorState state;

    private int counter;

    private RepositoryService repository;

    private final Logger logger = LoggerFactory.getLogger(getClass());

    /**
     * Instantiates a new navigator implementation.
     * 
     * Be aware however that there is a ServiceFactory which has a Navigator Singleton since this should be a Singleton.
     * So use with caution.
     */
    public NavigatorImpl() {

        this(null, NUMBER_OF_NAVIGATOR_THREADS);
    }

    /**
     * Instantiates a new navigator implementation.
     * 
     * Be aware however that there is a ServiceFactory which has a Navigator Singleton since this should be a Singleton.
     * So use with caution.
     * 
     * @param numberOfThreads
     *            the number of threads
     */
    public NavigatorImpl(int numberOfThreads) {

        this(null, numberOfThreads);
    }

    /**
     * Instantiates a new navigator implementation.
     * 
     * @param numberOfThreads
     *            the number of navigator threads
     * @param repo
     *            the process repository
     */
    public NavigatorImpl(RepositoryService repo, int numberOfThreads) {

        // TODO Lazy initialized, o rely?
        this.scheduler = new FIFOScheduler();
        this.executionThreads = new ArrayList<NavigationThread>();
        this.state = NavigatorState.INIT;
        this.counter = 0;
        this.navigatorThreads = numberOfThreads;
        // this.repository = ServiceFactory.getRepositoryService();
        this.repository = repo;
        this.suspendedTokens = new ArrayList<Token>();
        this.runningInstances = Collections.synchronizedList(new ArrayList<ProcessInstance>());
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

        // TODO use the variable repository here. This cannot be used in tests, as it requires the bootstrap to have
        // run first, but we definitely do not want to start the whole engine to test a simple feature.
        ProcessDefinition definition = ServiceFactory.getRepositoryService().getProcessDefinition(processID);

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

        ProcessDefinition definition = repository.getProcessDefinition(processID);

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
     * Stop the Navigator. So in fact you need to stop all the navigation threads.
     */
    @Override
    public void stop() {

        for (NavigationThread executionThread : executionThreads) {
            executionThread.setShouldStop(true);
        }
        changeState(NavigatorState.STOPPED);
    }

    @Override
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

        return (FIFOScheduler) scheduler;
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

        boolean instanceContained = runningInstances.remove(instance);

        // TODO maybe throw an exception if the instance provided is not in the running instances list?
        if (instanceContained) {
            finishedInstances.add(instance);
        }

        if (runningInstances.isEmpty()) {
            changeState(NavigatorState.CURRENTLY_FINISHED);
        }

    }

    @Override
    public final NavigatorStatistic getStatistics() {

        final NavigatorStatistic stat = new NavigatorStatistic(getEndedInstances().size(),
            getRunningInstances().size(), this.executionThreads.size(), isIdle());

        return stat;
    }
}
