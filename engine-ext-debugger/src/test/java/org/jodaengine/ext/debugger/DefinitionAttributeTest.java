package org.jodaengine.ext.debugger;

import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import org.jodaengine.exception.IllegalStarteventException;
import org.jodaengine.ext.debugging.DebuggerDefinitionAttribute;
import org.jodaengine.process.definition.ProcessDefinition;
import org.mockito.ArgumentCaptor;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

/**
 * Tests the definition attribute functionality.
 * 
 * @author Jan Rehwaldt
 * @since 2011-05-24
 */
public class DefinitionAttributeTest {
    
    /**
     * Setup.
     */
    @BeforeClass
    public void setUp() {
        
    }
    
    /**
     * Tests the enabling of debugging for a {@link ProcessDefinition}.
     */
    @Test
    public void testEnablingDebugging() {
        DebuggerDefinitionAttribute attribute = new DebuggerDefinitionAttribute();
        Assert.assertFalse(attribute.isDebuggingEnabled());
        
        attribute.enable();
        Assert.assertTrue(attribute.isDebuggingEnabled());
        
        attribute.disable();
        Assert.assertFalse(attribute.isDebuggingEnabled());
    }
    
    /**
     * Tests the creation and binding of a {@link DebuggerDefinitionAttribute} to a {@link ProcessDefinition}.
     */
    @Test
    public void testCreationAndBindingOfAnAttribute() {
        ProcessDefinition definition = mock(ProcessDefinition.class);
        
        DebuggerDefinitionAttribute attribute = DebuggerDefinitionAttribute.getAttribute(definition);
        Assert.assertNotNull(attribute);
        
        ArgumentCaptor<DebuggerDefinitionAttribute> attributeCap
            = ArgumentCaptor.forClass(DebuggerDefinitionAttribute.class);
        
        verify(definition).setAttribute(eq(DebuggerDefinitionAttribute.ATTRIBUTE_KEY), attributeCap.capture());
        Assert.assertNotNull(attributeCap.getValue());
        Assert.assertEquals(attributeCap.getValue(), attribute);
    }
}
