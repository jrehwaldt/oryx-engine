package org.jodaengine.eventmanagement.timing;

import org.jodaengine.eventmanagement.adapter.configuration.PullAdapterConfiguration;
import org.jodaengine.eventmanagement.adapter.error.ErrorAdapter;
import org.jodaengine.eventmanagement.adapter.incoming.InboundPullAdapter;
import org.jodaengine.eventmanagement.adapter.mail.InboundMailAdapterConfiguration;
import org.jodaengine.exception.JodaEngineException;
import org.mockito.Mockito;
import org.quartz.SchedulerException;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

/**
 * Test class for {@link QuartzJobManager}.
 */
public class QuartzJobManagerTest {

    private static final int PULL_TIMEOUT = 5;
    private static final short VERIFY_FACTOR = 4;
    private static final long TIMER = 100;

    private QuartzJobManager quartzJobManager = null;

    /**
     * Setup.
     * 
     * @throws SchedulerException
     *             test fails
     */
    @BeforeMethod
    public void beforeMethod()
    throws SchedulerException {

        ErrorAdapter errorAdapter = Mockito.mock(ErrorAdapter.class);
        // CorrelationManagerImpl correlation = mock(CorrelationManagerImpl.class);
        this.quartzJobManager = new QuartzJobManager(errorAdapter);
        quartzJobManager.start();
    }

    /**
     * Cleanup.
     */
    @AfterMethod
    public void afterMethod() {

        quartzJobManager.stop();
        this.quartzJobManager = null;
    }

    /**
     * Tests the registering of a pull adapter and its invocation.
     * 
     * @throws JodaEngineException
     *             test fails if either pulling or registering fails
     */
    @Test
    public void testRegisteringAPullAdapter()
    throws JodaEngineException {

        InboundPullAdapter adapter = Mockito.mock(InboundPullAdapter.class);
        // Unfortunately mocking doesn't seem to work with classes as return value,
        // therefore the PullAdapterConfiguration is instantiated manually
        PullAdapterConfiguration configuration = new InboundMailAdapterConfiguration(null, null, null, null, 0, false);
        Mockito.when(adapter.getConfiguration()).thenReturn(configuration);
        this.quartzJobManager.registerJobForInboundPullAdapter(adapter);

        Mockito.verify(adapter, Mockito.timeout(PULL_TIMEOUT * VERIFY_FACTOR).atLeastOnce()).pull();
    }

    // /**
    // * Test registering a non recurring event, in this case a simple timer.
    // * The test waits some time, until the timer should be done and then an assertion is used to test
    // * if resume was called on the token.
    // *
    // * @throws JodaEngineException the JodaEngine exception
    // * @throws InterruptedException the interrupted exception for thread sleeping
    // */
    // @Test
    // public void testRegisteringANonRecurringEvent() throws JodaEngineException, InterruptedException {
    //
    // Token token = mock(TokenImpl.class);
    // TimerConfiguration configuration = new TimerAdapterConfiguration(TIMER);
    // this.timer.registerNonRecurringJob(configuration, token);
    //
    // Thread.sleep(TIMER + TIMER);
    // verify(token).resume();
    // }

    // /**
    // * Test registering a non recurring event, in this case a simple timer. Because waiting time is not long enough,
    // * the token should not be resumed.
    // *
    // * @throws JodaEngineException the JodaEngine exception
    // * @throws InterruptedException the interrupted exception for thread sleeping
    // */
    // @Test
    // public void testFailingRegisteringANonRecurringEvent() throws JodaEngineException, InterruptedException {
    // Token token = mock(TokenImpl.class);
    // TimerConfiguration configuration = new TimerAdapterConfiguration(TIMER);
    // this.timer.registerNonRecurringJob(configuration, token);
    // verify(token, never()).resume();
    // // Wait until job is deleted, otherwise conflicts can occur
    // Thread.sleep(TIMER + TIMER);
    // }

}
