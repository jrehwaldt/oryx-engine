package org.jodaengine.process.instance;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

import java.util.ArrayList;

import org.jodaengine.process.structure.ControlFlow;
import org.jodaengine.process.structure.Node;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;


/**
 * The Class ProcessInstanceContextImplTest.
 */
public class ProcessInstanceContextImplTest {

    /** The context. */
    private ProcessInstanceContext context;

    /**
     * Sets the waiting execution with empty waiting {@link ControlFlow}s. This is the second part of the method which is tested.
     * For a given {@link ControlFlow}, the destination node is used as key. A List with the {@link ControlFlow} is expected as return
     * value.
     */
    @Test
    public void setWaitingExecutionWithEmptyWaitingControlFlows() {

        ControlFlow t = mock(ControlFlow.class);

        Node n = mock(Node.class);
        when(t.getDestination()).thenReturn(n);

        context.setSignaledControlFlow(t);

        ArrayList<ControlFlow> expected = new ArrayList<ControlFlow>();
        expected.add(t);
        assertEquals(context.getSignaledControlFlow(n), expected);

    }

    /**
     * Sets the waiting execution. This is the first part of the method which is tested. See explanation above.
     */
    @Test
    public void setWaitingExecution() {

        ControlFlow t = mock(ControlFlow.class);
        ControlFlow t2 = mock(ControlFlow.class);

        Node n = mock(Node.class);
        when(t.getDestination()).thenReturn(n);
        when(t2.getDestination()).thenReturn(n);

        context.setSignaledControlFlow(t);
        context.setSignaledControlFlow(t2);

        ArrayList<ControlFlow> expected = new ArrayList<ControlFlow>();
        expected.add(t);
        expected.add(t2);
        assertEquals(context.getSignaledControlFlow(n), expected);
    }

    /**
     * Removes the incoming {@link ControlFlow}s signaled.
     */
    @Test
    public void removeIncomingControlFlowsSignaled() {

        // Create a {@link ControlFlow} mock with a Destination Node as Mock
        ControlFlow t = mock(ControlFlow.class);
        Node n = mock(Node.class);
        when(t.getDestination()).thenReturn(n);

        // Save the {@link ControlFlow} in the context Map
        context.setSignaledControlFlow(t);

        // Build a list out of it
        ArrayList<ControlFlow> controlFlowList = new ArrayList<ControlFlow>();
        controlFlowList.add(t);

        // Return the list, if for incoming {@link ControlFlow}s is asked
        when(n.getIncomingControlFlows()).thenReturn(controlFlowList);

        // The method to test, finally...
        context.removeSignaledControlFlows(n);

        // Now the list should be empty, because the remove private method was called in the context
        assertEquals(context.getSignaledControlFlow(n).size(), 0);
    }

    /**
     * All incoming {@link ControlFlow}s signaled - but without signaled {@link ControlFlow}s.
     */
    @Test
    public void allIncomingControlFlowsSignaledWithoutSignaledControlFlows() {

        Node n = mock(Node.class);
        assertFalse(context.allIncomingControlFlowsSignaled(n), "should not contain all incoming controlFlows");
    }

    /**
     * All incoming {@link ControlFlow}s are signaled and therefore true should be returned .
     */
    @Test
    public void allIncomingControlFlowsSignaled() {

        // Create a {@link ControlFlow} mock with a Destination Node as Mock
        ControlFlow t = mock(ControlFlow.class);
        ControlFlow t2 = mock(ControlFlow.class);
        
        Node n = mock(Node.class);
        
        when(t.getDestination()).thenReturn(n);
        when(t2.getDestination()).thenReturn(n);
        
        context.setSignaledControlFlow(t);
        context.setSignaledControlFlow(t2);

        // Build a list out of it
        ArrayList<ControlFlow> controlFlowList = new ArrayList<ControlFlow>();
        controlFlowList.add(t);

        // Return the list, if for incoming {@link ControlFlow}s is asked
        when(n.getIncomingControlFlows()).thenReturn(controlFlowList);

        assertTrue(context.allIncomingControlFlowsSignaled(n), "should contain all incoming controlFlows");
    }

    /**
     * Before class.
     */
    @BeforeClass
    public void beforeClass() {

        context = new ProcessInstanceContextImpl();
    }

    /**
     * After class.
     */
    @AfterClass
    public void afterClass() {
        
        context = null;

    }

}
