package org.jodaengine.ext.debugging.shared;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import javax.el.PropertyNotFoundException;

import org.jodaengine.ext.debugging.api.BreakpointCondition;
import org.jodaengine.process.instance.AbstractProcessInstance;
import org.jodaengine.process.instance.ProcessInstanceContext;
import org.jodaengine.process.token.Token;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

/**
 * Tests the {@link JuelBreakpointCondition} functionality.
 * 
 * @author Jan Rehwaldt
 * @since 2011-06-01
 */
public class JuelBreakpointConditionTest {
    
    private Token mockToken;
    private AbstractProcessInstance mockInstance;
    private ProcessInstanceContext mockContext;
    
    /**
     * Setup.
     */
    @BeforeClass
    public void setUp() {
        this.mockToken = mock(Token.class);
        this.mockInstance = mock(AbstractProcessInstance.class);
        this.mockContext = mock(ProcessInstanceContext.class);

        when(this.mockToken.getInstance()).thenReturn(this.mockInstance);
        when(this.mockInstance.getContext()).thenReturn(this.mockContext);
        when(this.mockContext.getVariable("found")).thenReturn("wahr");
        when(this.mockContext.getVariable("notfound")).thenThrow(new PropertyNotFoundException());
    }
    
    /**
     * Tests that a {@link PropertyNotFoundException} will cause the condition to be true.
     */
    @Test
    public void testPropertyNotFoundConditionBehaviour() {
        
        BreakpointCondition conditionNotFound = new JuelBreakpointCondition("#{notfound != \"wahr\"}");
        Assert.assertTrue(conditionNotFound.evaluate(this.mockToken));
        
        BreakpointCondition conditionMalformed = new JuelBreakpointCondition("#{found is wrong formated}");
        Assert.assertTrue(conditionMalformed.evaluate(this.mockToken));
    }
    
    /**
     * Tests that a {@link PropertyNotFoundException} will cause the condition to be true.
     */
    @Test
    public void testThePropertyIsCorrectlyEvaluated() {
        
        BreakpointCondition conditionTrue = new JuelBreakpointCondition("#{found == \"wahr\"}");
        Assert.assertTrue(conditionTrue.evaluate(this.mockToken));
        
        BreakpointCondition conditionFalse = new JuelBreakpointCondition("#{found != \"wahr\"}");
        Assert.assertFalse(conditionFalse.evaluate(this.mockToken));
    }
}
