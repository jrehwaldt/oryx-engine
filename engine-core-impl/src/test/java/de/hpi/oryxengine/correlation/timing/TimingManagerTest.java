package de.hpi.oryxengine.correlation.timing;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.timeout;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.quartz.SchedulerException;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import de.hpi.oryxengine.correlation.CorrelationManagerImpl;
import de.hpi.oryxengine.correlation.adapter.InboundPullAdapter;
import de.hpi.oryxengine.correlation.adapter.PullAdapterConfiguration;
import de.hpi.oryxengine.exception.DalmatinaException;;

/**
 * Test class for {@link TimingManagerImpl}.
 */
public class TimingManagerTest {
    
    private static final int PULL_TIMEOUT = 5;
    private static final short VERIFY_FACTOR = 4;
    
    private TimingManager timer = null;
    
    /**
     * Tests the registering of a pull adapter and its invocation.
     * 
     * @throws DalmatinaException test fails if either pulling or registering fails
     */
    @Test
    public void testRegisteringAPullAdapter() throws DalmatinaException {
        InboundPullAdapter adapter = mock(InboundPullAdapter.class);
        PullAdapterConfiguration configuration = mock(PullAdapterConfiguration.class);
        
        when(configuration.getPullInterval()).thenReturn((long) PULL_TIMEOUT);
        when(configuration.getUniqueName()).thenReturn("a-unique-name");
        
        when(adapter.getConfiguration()).thenReturn(configuration);
        this.timer.registerPullAdapter(adapter);
        
        verify(adapter, timeout(PULL_TIMEOUT * VERIFY_FACTOR).atLeastOnce()).pull();
    }

    /**
     * Setup.
     * @throws SchedulerException test fails
     */
    @BeforeMethod
    public void beforeMethod()
    throws SchedulerException {
        CorrelationManagerImpl correlation = mock(CorrelationManagerImpl.class);
        this.timer = new TimingManagerImpl(correlation);
    }

    /**
     * Cleanup.
     */
    @AfterMethod
    public void afterMethod() {
        this.timer = null;
    }

}
