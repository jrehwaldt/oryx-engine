package org.jodaengine.ext.debugging.rest;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Collection;

import org.codehaus.jackson.map.type.TypeFactory;
import org.jodaengine.ext.debugging.DebuggerServiceImpl;
import org.jodaengine.ext.debugging.api.Breakpoint;
import org.jodaengine.ext.service.ExtensionNotAvailableException;
import org.jodaengine.util.testing.AbstractJsonServerTest;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

/**
 * Tests the implementation of our rest {@link DebuggerWebService}.
 * 
 * @author Jan Rehwaldt
 * @since 2011-06-04
 */
public class DebuggerWebServiceTest extends AbstractJsonServerTest {
    
    private DebuggerServiceImpl debugger;
    
    /**
     * Setup.
     * 
     * @throws ExtensionNotAvailableException test fails
     */
    @BeforeClass
    public void setUp() throws ExtensionNotAvailableException {
        this.debugger = this.jodaEngineServices.getExtensionService().getExtensionService(
            DebuggerServiceImpl.class,
            DebuggerServiceImpl.DEBUGGER_SERVICE_NAME);
        
        Assert.assertNotNull(this.debugger);
    }
    
    @Override
    protected DebuggerWebService getResourceSingleton() {
        return new DebuggerWebService(this.jodaEngineServices);
    }
    
    
    /**
     * Tests the get statistic method with json deserialization.
     * 
     * @throws URISyntaxException
     *             test fails
     * @throws IOException
     *             test fails
     */
    @Test
    public void testGetBreakpoints()
    throws URISyntaxException, IOException {
        String json = makeGETRequestReturningJson("/debugger/breakpoints");
        Assert.assertNotNull(json);
        
        Collection<Breakpoint> breakpoints = this.debugger.getBreakpoints();
        Collection<Breakpoint> callBreakpoints = this.mapper.readValue(
            json,
            TypeFactory.collectionType(Collection.class, Breakpoint.class));
        Assert.assertNotNull(callBreakpoints);
        
        Assert.assertEquals(breakpoints.size(), callBreakpoints.size());
        //
        // TODO better, faster, smarter, rock-solid
        //
    }
}
