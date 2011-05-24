package org.jodaengine.ext.debugger;

import org.jodaengine.ext.debugging.Breakpoint;
import org.jodaengine.process.structure.Node;
import org.jodaengine.process.structure.NodeImpl;
import org.jodaengine.process.token.Token;
import org.jodaengine.process.token.TokenImpl;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

/**
 * Tests the {@link Breakpoint} functionality.
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
        this.token = new TokenImpl(this.node, null, null);
    }
    
    /**
     * Set up each method.
     */
    @BeforeMethod
    public void setUpMethod() {
        this.breakpoint = Breakpoint.getAttribute(this.node);
        Assert.assertNotNull(this.breakpoint);
        Assert.assertEquals(this.breakpoint.getNode(), this.node);
    }
    
    /**
     * Tests that a {@link Breakpoint} will be created on Breakpoint#getAttribute(Node).
     */
    @Test
    public void testBreakpointIsCreated() {
        Assert.assertNotNull(this.breakpoint);
    }
    
    /**
     * Tests that a new {@link Breakpoint} is enabled by default.
     */
    @Test
    public void testBreakpointEnabledOnDefault() {
        Assert.assertTrue(this.breakpoint.isEnabled());
    }
    
    /**
     * Tests that a {@link Breakpoint} will match if a {@link Token} is on the
     * breakpoint's {@link Node}.
     */
    @Test
    public void testBreakpointWillMatchWhenTokenOnNode() {
        Assert.assertTrue(this.breakpoint.matches(this.token));
    }
    
    /**
     * Tests that a disabled {@link Breakpoint} will not match if a {@link Token} is on the
     * breakpoint's {@link Node}.
     */
    @Test
    public void testDisabledBreakpointWillNotMatchWhenTokenOnNode() {
        this.breakpoint.disable();
        Assert.assertFalse(this.breakpoint.matches(this.token));
    }
    
    /**
     * Tests that a disabled {@link Breakpoint} will not match if a {@link Token} is on the
     * breakpoint's {@link Node}.
     */
    @Test
    public void testBreakpointWillNotMatchWhenTokenOnOtherNode() {
        Node otherNode = new NodeImpl(null, null, null);
        Token otherToken = new TokenImpl(otherNode, null, null);
        Assert.assertNotSame(this.breakpoint.getNode(), otherNode);
        Assert.assertFalse(this.breakpoint.matches(otherToken));
    }
    
    /**
     * Tests that no instance is created when calling getAttributeIfExists().
     */
    @Test
    public void testBreakpointWillBeNullIfNoneExists() {
        Node otherNode = new NodeImpl(null, null, null);
        Assert.assertNull(Breakpoint.getAttributeIfExists(otherNode));
    }
}
