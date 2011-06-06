package org.jodaengine.ext.debugging.rest;

import java.io.IOException;
import java.io.Serializable;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.Collection;

import org.codehaus.jackson.map.type.TypeFactory;
import org.jodaengine.ext.debugging.DebuggerServiceImpl;
import org.jodaengine.ext.debugging.api.Breakpoint;
import org.jodaengine.ext.debugging.api.DebuggerCommand;
import org.jodaengine.ext.debugging.shared.BreakpointImpl;
import org.jodaengine.ext.debugging.shared.DebuggerAttribute;
import org.jodaengine.ext.debugging.shared.InterruptedInstanceImpl;
import org.jodaengine.ext.debugging.shared.JuelBreakpointCondition;
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
     * Tests that the shared data structures are serializable and deserializable.
     */
    @Test
    public void testSharedDatastructuresAreSerializableAndDeserializable() {
        @SuppressWarnings("unchecked")
        Collection<Class<? extends Serializable>> types = Arrays.asList(
            DebuggerCommand.class,
            BreakpointImpl.class,
            InterruptedInstanceImpl.class,
            DebuggerAttribute.class,
            JuelBreakpointCondition.class);
        
        for (Class<?> type: types) {
            Assert.assertTrue(this.mapper.canSerialize(type));
            Assert.assertTrue(this.mapper.canDeserialize(TypeFactory.type(type)));
        }
    }
    
    /**
     * Tests the get statistic method with json deserialization.
     * 
     * @throws URISyntaxException test fails
     * @throws IOException test fails
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
        
        for (Breakpoint breakpoint: breakpoints) {
            Assert.assertTrue(callBreakpoints.contains(breakpoint));
        }
    }
}
