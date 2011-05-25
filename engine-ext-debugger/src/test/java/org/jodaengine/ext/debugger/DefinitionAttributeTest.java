package org.jodaengine.ext.debugger;

import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import org.jodaengine.ext.debugger.util.AttributeKeyProvider;
import org.jodaengine.ext.debugging.shared.DebuggerAttribute;
import org.jodaengine.process.definition.ProcessDefinition;
import org.mockito.ArgumentCaptor;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * Tests the {@link DebuggerAttribute} functionality.
 * 
 * @author Jan Rehwaldt
 * @since 2011-05-24
 */
public class DefinitionAttributeTest {
    
    /**
     * Tests the enabling of debugging for a {@link ProcessDefinition}.
     */
    @Test
    public void testEnablingDebugging() {
        DebuggerAttribute attribute = new DebuggerAttribute();
        Assert.assertFalse(attribute.isEnabled());
        
        attribute.enable();
        Assert.assertTrue(attribute.isEnabled());
        
        attribute.disable();
        Assert.assertFalse(attribute.isEnabled());
    }
    
    /**
     * Tests that a valid id is specified.
     */
    @Test
    public void testIdNotNull() {
        DebuggerAttribute attribute = new DebuggerAttribute();
        Assert.assertNotNull(attribute.getID());
    }
    
    /**
     * Tests the creation and binding of a {@link DebuggerAttribute} to a {@link ProcessDefinition}.
     */
    @Test
    public void testCreationAndBindingOfAnAttribute() {
        ProcessDefinition definition = mock(ProcessDefinition.class);
        
        DebuggerAttribute attribute = DebuggerAttribute.getAttribute(definition);
        Assert.assertNotNull(attribute);
        
        ArgumentCaptor<DebuggerAttribute> attributeCap = ArgumentCaptor.forClass(DebuggerAttribute.class);
        
        verify(definition).setAttribute(eq(AttributeKeyProvider.getAttributeKey()), attributeCap.capture());
        Assert.assertNotNull(attributeCap.getValue());
        Assert.assertEquals(attributeCap.getValue(), attribute);
    }
}
