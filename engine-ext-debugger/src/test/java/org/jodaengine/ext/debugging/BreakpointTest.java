package org.jodaengine.ext.debugging;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.jodaengine.ext.debugging.api.Breakpoint;
import org.jodaengine.ext.debugging.api.BreakpointCondition;
import org.jodaengine.ext.debugging.shared.BreakpointImpl;
import org.jodaengine.ext.debugging.shared.JuelBreakpointCondition;
import org.jodaengine.process.instance.AbstractProcessInstance;
import org.jodaengine.process.structure.Node;
import org.jodaengine.process.structure.NodeImpl;
import org.jodaengine.process.token.BpmnToken;
import org.jodaengine.process.token.Token;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

/**
 * Tests the {@link BreakpointImpl} functionality.
 * 
 * @author Jan Rehwaldt
 * @since 2011-05-24
 */
public class BreakpointTest {
    
    private Breakpoint breakpoint;
    private Token token;
    private Node node;
    private AbstractProcessInstance instance;
    
    /**
     * Set up whole class.
     */
    @BeforeClass
    public void setUpClass() {
        this.node = mock(Node.class);
        this.instance = mock(AbstractProcessInstance.class);
        this.token = mock(Token.class);
        when(this.token.getCurrentNode()).thenReturn(this.node);
        when(this.token.getInstance()).thenReturn(this.instance);
    }
    
    /**
     * Set up each method.
     */
    @BeforeMethod
    public void setUpMethod() {
        this.breakpoint = new BreakpointImpl(this.node);
    }
    
    /**
     * Tests that a new {@link BreakpointImpl} is enabled by default.
     */
    @Test
    public void testBreakpointEnabledOnDefault() {
        Assert.assertTrue(this.breakpoint.isEnabled());
    }
    
    /**
     * Tests that a {@link BreakpointImpl} will match if a {@link Token} is on the
     * breakpoint's {@link Node}.
     */
    @Test
    public void testBreakpointWillMatchWhenTokenOnNode() {
        Assert.assertTrue(this.breakpoint.matches(this.token));
    }
    
    /**
     * Tests that a disabled {@link BreakpointImpl} will not match if a {@link Token} is on the
     * breakpoint's {@link Node}.
     */
    @Test
    public void testDisabledBreakpointWillNotMatchWhenTokenOnNode() {
        this.breakpoint.disable();
        Assert.assertFalse(this.breakpoint.matches(this.token));
    }
    
    /**
     * Tests that a disabled {@link BreakpointImpl} will not match if a {@link Token} is on the
     * breakpoint's {@link Node}.
     */
    @Test
    public void testBreakpointWillNotMatchWhenTokenOnOtherNode() {
        Node otherNode = new NodeImpl(null, null, null);
        Token otherToken = new BpmnToken(otherNode, null, null);
        Assert.assertNotSame(this.breakpoint.getNode(), otherNode);
        Assert.assertFalse(this.breakpoint.matches(otherToken));
    }
    
    /**
     * Tests the proper evaluation of a breakpoint with true condition.
     */
    @Test
    public void testBreakpointWithTrueCondition() {
        BreakpointCondition condition = new JuelBreakpointCondition("true");
        this.breakpoint.setCondition(condition);
        Assert.assertTrue(this.breakpoint.matches(this.token));
    }
    
    /**
     * Tests the proper evaluation of a breakpoint with false condition.
     */
    @Test
    public void testBreakpointWithFalseCondition() {
        BreakpointCondition condition = new JuelBreakpointCondition("false");
        this.breakpoint.setCondition(condition);
        Assert.assertFalse(this.breakpoint.matches(this.token));
    }
}
