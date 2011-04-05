package de.hpi.oryxengine.plugin.navigator;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import de.hpi.oryxengine.navigator.NavigatorImpl;
import de.hpi.oryxengine.navigator.NavigatorState;

/**
 * Test class for various navigator plugin tests.
 */
@ContextConfiguration(locations = "/test.oryxengine.cfg.xml")
@DirtiesContext(classMode = ClassMode.AFTER_EACH_TEST_METHOD)
public class NavigatorListenerTest extends AbstractTestNGSpringContextTests {
    
    /** The navigator. */
    private NavigatorImpl navigator = null;
    
    /** The mock. */
    private AbstractNavigatorListener mock = null;
    
    /**
     * Setup method.
     */
    @BeforeTest
    public void setUp() {
        
        this.navigator = new NavigatorImpl();
        this.mock = mock(AbstractNavigatorListener.class);
        this.navigator.registerPlugin(mock);
        navigator.start();
    }
    
    /**
     * Tests that the plugin is called when navigator starts.
     */
    @Test
    public void testStartedTrigger() {
        verify(mock).update(navigator, NavigatorState.RUNNING);
    }
    
    /**
     * Tests that the plugin is called when navigator stops.
     */
    @Test
    public void testStopTrigger() {
        navigator.stop();
        verify(mock).update(navigator, NavigatorState.STOPPED);
    }
}
