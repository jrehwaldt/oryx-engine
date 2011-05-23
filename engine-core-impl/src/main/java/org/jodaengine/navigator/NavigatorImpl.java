package org.jodaengine.navigator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.annotation.Nonnull;

import org.jodaengine.JodaEngineServices;
import org.jodaengine.RepositoryServiceInside;
import org.jodaengine.bootstrap.JodaEngine;
import org.jodaengine.bootstrap.Service;
import org.jodaengine.eventmanagement.subscription.ProcessStartEvent;
import org.jodaengine.exception.DefinitionNotFoundException;
import org.jodaengine.ext.AbstractPluggable;
import org.jodaengine.ext.listener.AbstractNavigatorListener;
import org.jodaengine.navigator.schedule.FIFOScheduler;
import org.jodaengine.navigator.schedule.Scheduler;
import org.jodaengine.process.definition.ProcessDefinitionID;
import org.jodaengine.process.definition.ProcessDefinitionInside;
import org.jodaengine.process.instance.AbstractProcessInstance;
import org.jodaengine.process.token.Token;


/**
 * The Class NavigatorImpl. Our Implementation of the Navigator.
 */
public class NavigatorImpl extends AbstractPluggable<AbstractNavigatorListener>
implements Navigator, NavigatorInside, Service {

    private static final int NUMBER_OF_NAVIGATOR_THREADS = 10;

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
    private List<AbstractProcessInstance> runningInstances;

    /**
     * All the process instances that finished i.e. that reached all their end events. Temporary until we can save them,
     * the way they are implemented now, they eat up a huge amount of heap space, as they are the last reference to
     * stuff the garbage collector would normally eat.
     */
    private List<AbstractProcessInstance> finishedInstances;

    /** The execution threads. Yes our navigator is multi-threaded. Pretty awesome. */
    private ArrayList<NavigationThread> executionThreads;

    private int navigatorThreads;

    private NavigatorState state;

    private int counter;

    private RepositoryServiceInside repository;

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
     * @param repository
     *            the process repository
     */
    public NavigatorImpl(RepositoryServiceInside repository, int numberOfThreads) {

        this.scheduler = new FIFOScheduler();
        this.executionThreads = new ArrayList<NavigationThread>();
        this.state = NavigatorState.INIT;
        this.counter = 0;
        this.navigatorThreads = numberOfThreads;
        // this.repository = ServiceFactory.getRepositoryService();
        this.repository = repository;
        this.suspendedTokens = new ArrayList<Token>();
        this.runningInstances = Collections.synchronizedList(new ArrayList<AbstractProcessInstance>());
        this.finishedInstances = new ArrayList<AbstractProcessInstance>();
    }

    /**
     * Start. Starts the number of worker thread specified in the NUMBER_OF_NAVIGATOR_THREADS Constant and adds them to
     * the execution threads list.
     * 
     * @param services the {@link JodaEngine} instance
     */
    @Override
    public void start(JodaEngineServices services) {

        // "Gentlemen, start your engines"
        for (int i = 0; i < navigatorThreads; i++) {
            addThread();
        }
        changeState(NavigatorState.RUNNING);
    }
    
    @Override
    public boolean isRunning() {
        return NavigatorState.RUNNING.equals(this.state);
    }

    @Override
    public void addThread() {

        NavigationThread thread = new NavigationThread(String.format("NT %d", counter), scheduler);
        thread.start();
        executionThreads.add(thread);
        counter++;
    }

    @Override
    public AbstractProcessInstance startProcessInstance(ProcessDefinitionID processID)
    throws DefinitionNotFoundException {

        // TODO use the variable repository here. This cannot be used in tests, as it requires the bootstrap to have
        // run first, but we definitely do not want to start the whole engine to test a simple feature.
        ProcessDefinitionInside definition = repository.getProcessDefinitionInside(processID);
        AbstractProcessInstance instance = definition.createProcessInstance(this);

        // for (Node node : definition.getStartNodes()) {
        // Token newToken = instance.createToken(node, this);
        // startArbitraryInstance(newToken);
        // }
        runningInstances.add(instance);

        return instance;
    }

    @Override
    public AbstractProcessInstance startProcessInstance(ProcessDefinitionID processID, ProcessStartEvent event)
    throws DefinitionNotFoundException {

        ProcessDefinitionInside definition = repository.getProcessDefinitionInside(processID);
        AbstractProcessInstance instance = definition.createProcessInstance(this);
        
        runningInstances.add(instance);

        return instance;
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

        scheduler.submit(token);
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
    public List<AbstractProcessInstance> getRunningInstances() {

        return runningInstances;
    }

    @Override
    public List<AbstractProcessInstance> getEndedInstances() {

        return finishedInstances;
    }
    
    @Override
    public void cancelProcessInstance(AbstractProcessInstance instance) {

        instance.cancel();
        signalEndedProcessInstance(instance);
        
    }

    @Override
    public void signalEndedProcessInstance(AbstractProcessInstance instance) {

        boolean instanceContained = runningInstances.remove(instance);

        // TODO maybe throw an exception if the instance provided is not in the running instances list?
        if (instanceContained) {
            finishedInstances.add(instance);
        }

        if (runningInstances.isEmpty()) {
            changeState(NavigatorState.IDLE);
        }

    }

    @Override
    public final NavigatorStatistic getStatistics() {

        final NavigatorStatistic stat = new NavigatorStatistic(getEndedInstances().size(),
            getRunningInstances().size(), this.executionThreads.size(), isIdle());

        return stat;
    }
}
