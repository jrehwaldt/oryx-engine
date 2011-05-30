package org.jodaengine.ext.debugging;

import org.jodaengine.ext.debugging.api.Breakpoint;
import org.jodaengine.ext.debugging.api.BreakpointCondition;
import org.jodaengine.ext.debugging.shared.BreakpointImpl;
import org.jodaengine.ext.debugging.shared.JuelBreakpointCondition;
import org.jodaengine.process.instance.AbstractProcessInstance;
import org.jodaengine.process.instance.ProcessInstance;
import org.jodaengine.process.structure.Node;
import org.jodaengine.process.structure.NodeImpl;
import org.jodaengine.process.token.BpmnToken;
import org.jodaengine.process.token.Token;
import org.jodaengine.process.token.builder.BpmnTokenBuilder;
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
        // TODO @Gerardo(CodeReview) Das schreit ja geradezu nach Mock, aber daran hast du sicherlich schon gedacht?
        this.node = new NodeImpl(null, null, null);
        AbstractProcessInstance instance = new ProcessInstance(null, new BpmnTokenBuilder(null, null, this.node));
        this.token = new BpmnToken(this.node, instance, null);
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
