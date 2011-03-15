package de.hpi.oryxengine.process.instance;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

import java.util.ArrayList;

import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import de.hpi.oryxengine.process.structure.Node;
import de.hpi.oryxengine.process.structure.Transition;

// TODO: Auto-generated Javadoc
/**
 * The Class ProcessInstanceContextImplTest.
 */
public class ProcessInstanceContextImplTest {

    /** The context. */
    private ProcessInstanceContext context;

    /**
     * Sets the waiting execution with empty waiting transitions. This is the second part of the method which is tested.
     * For a given transition, the destination node is used as key. A List with the Transition is expected as return
     * value.
     */
    @Test
    public void setWaitingExecutionWithEmptyWaitingTransitions() {

        Transition t = mock(Transition.class);

        Node n = mock(Node.class);
        when(t.getDestination()).thenReturn(n);

        context.setWaitingExecution(t);

        ArrayList<Transition> expected = new ArrayList<Transition>();
        expected.add(t);
        assertEquals(context.getWaitingExecutions(n), expected);

    }

    /**
     * Sets the waiting execution. This is the first part of the method which is tested. See explanation above.
     */
    @Test
    public void setWaitingExecution() {

        Transition t = mock(Transition.class);
        Transition t2 = mock(Transition.class);

        Node n = mock(Node.class);
        when(t.getDestination()).thenReturn(n);
        when(t2.getDestination()).thenReturn(n);

        context.setWaitingExecution(t);
        context.setWaitingExecution(t2);

        ArrayList<Transition> expected = new ArrayList<Transition>();
        expected.add(t);
        expected.add(t2);
        assertEquals(context.getWaitingExecutions(n), expected);
    }

    /**
     * Removes the incoming transitions signaled.
     */
    @Test
    public void removeIncomingTransitionsSignaled() {

        // Create a transition mock with a Destination Node as Mock
        Transition t = mock(Transition.class);
        Node n = mock(Node.class);
        when(t.getDestination()).thenReturn(n);

        // Save the transition in the context Map
        context.setWaitingExecution(t);

        // Build a list out of it
        ArrayList<Transition> transitionList = new ArrayList<Transition>();
        transitionList.add(t);

        // Return the list, if for incoming Transitions is asked
        when(n.getIncomingTransitions()).thenReturn(transitionList);

        // The method to test, finally...
        context.removeIncomingTransitions(n);

        // Now the list should be empty, because the remove private method was called in the context
        assertEquals(context.getWaitingExecutions(n).size(), 0);
    }

    /**
     * All incoming transitions signaled - but without signaled transitions.
     */
    @Test
    public void allIncomingTransitionsSignaledWithoutSignaledTransitions() {

        Node n = mock(Node.class);
        assertFalse(context.allIncomingTransitionsSignaled(n), "should not contain all incoming transitions");
    }

    /**
     * All incoming transitions are signaled and therefore true should be returned .
     */
    @Test
    public void allIncomingTransitionsSignaled() {

        // Create a transition mock with a Destination Node as Mock
        Transition t = mock(Transition.class);
        Transition t2 = mock(Transition.class);
        
        Node n = mock(Node.class);
        
        when(t.getDestination()).thenReturn(n);
        when(t2.getDestination()).thenReturn(n);
        
        context.setWaitingExecution(t);
        context.setWaitingExecution(t2);

        // Build a list out of it
        ArrayList<Transition> transitionList = new ArrayList<Transition>();
        transitionList.add(t);

        // Return the list, if for incoming Transitions is asked
        when(n.getIncomingTransitions()).thenReturn(transitionList);

        assertTrue(context.allIncomingTransitionsSignaled(n), "should contain all incoming transitions");
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
