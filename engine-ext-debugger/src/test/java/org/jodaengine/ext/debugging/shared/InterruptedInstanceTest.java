package org.jodaengine.ext.debugging.shared;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.jodaengine.ext.debugging.api.Breakpoint;
import org.jodaengine.ext.debugging.api.InterruptedInstance;
import org.jodaengine.process.instance.AbstractProcessInstance;
import org.jodaengine.process.token.Token;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

/**
 * Tests the {@link InterruptedInstanceImpl} functionality.
 * 
 * @author Jan Rehwaldt
 * @since 2011-06-01
 */
public class InterruptedInstanceTest {
    
    private Breakpoint breakpoint;
    private Token token;
    private static final Object NULL_INSTANCE = null;
    
    /**
     * Setup.
     */
    @BeforeMethod
    public void setUp() {
        this.token = mock(Token.class);
        this.breakpoint = mock(Breakpoint.class);
    }
    
    /**
     * Tests the proper getting of a process instance.
     */
    @Test
    public void testGettingTheProcessInstance() {
        
        AbstractProcessInstance process = mock(AbstractProcessInstance.class);
        when(this.token.getInstance()).thenReturn(process);
        
        InterruptedInstance instance = new InterruptedInstanceImpl(this.token, this.breakpoint);
        Assert.assertNotNull(instance.getInterruptedInstance());
        Assert.assertEquals(process, instance.getInterruptedInstance());
    }
    
    /**
     * Tests the implementation of our hashCode() method.
     */
    @Test
    public void testHashCode() {
        
        InterruptedInstance instance1 = new InterruptedInstanceImpl(this.token, this.breakpoint);
        InterruptedInstance instance2 = new InterruptedInstanceImpl(this.token, this.breakpoint);
        
        //
        // they are not equal - otherwise the hash code needs to be equal, too
        //
        Assert.assertFalse(instance1.equals(instance2));
        Assert.assertFalse(instance1.equals(NULL_INSTANCE));
        Assert.assertTrue(instance1.equals(instance1));
        
        //
        // two different instance (id is different) have different hash codes
        //
        Assert.assertFalse(instance1.hashCode() == instance2.hashCode());
        
        //
        // the same instance returns the same hash code all the time
        //
        Assert.assertTrue(instance1.hashCode() == instance1.hashCode());
        Assert.assertTrue(instance2.hashCode() == instance2.hashCode());
    }
}
