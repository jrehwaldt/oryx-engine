package org.jodaengine.ext.debugging;

import org.jodaengine.ext.debugging.api.Breakpoint;
import org.jodaengine.ext.debugging.shared.BreakpointImpl;
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
    
    /**
     * Set up whole class.
     */
    @BeforeClass
    public void setUpClass() {
        this.node = new NodeImpl(null, null, null);
        this.token = new BpmnToken(this.node, null, null);
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
}
