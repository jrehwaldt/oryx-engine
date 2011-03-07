package de.hpi.oryxengine.plugin.navigator;

import static org.mockito.Mockito.mock;

import java.util.UUID;

import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import de.hpi.oryxengine.navigator.Navigator;
import de.hpi.oryxengine.navigator.NavigatorImpl;
import de.hpi.oryxengine.plugin.Pluggable;

/**
 * Test class for various navigator plugin tests.
 */
public class NavigatorListenerTest {
    
    /**
     * Setup method.
     */
    @BeforeTest
    public void setUp() {
        
    }
    
    /**
     * Tests that the plugin is really working.
     */
    @Test
    private void testNavigatorWorking() {
        NavigatorImpl navigator = new NavigatorImpl();
        navigator.registerPlugin(NavigatorListenerLogger.getInstance());
        
        
    }

    /**
     * Tear down method.
     */
    @AfterClass
    public void tearDown() {
        
    }
}
