package org.jodaengine.node.activity;

import static org.mockito.Mockito.mock;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import org.jodaengine.exception.JodaEngineException;
import org.jodaengine.navigator.Navigator;
import org.jodaengine.node.activity.custom.AutomatedDummyActivity;
import org.jodaengine.node.incomingbehaviour.SimpleJoinBehaviour;
import org.jodaengine.node.outgoingbehaviour.TakeAllSplitBehaviour;
import org.jodaengine.process.instance.BpmnProcessInstance;
import org.jodaengine.process.structure.Node;
import org.jodaengine.process.structure.NodeImpl;
import org.jodaengine.process.token.BPMNToken;
import org.jodaengine.process.token.BpmnTokenImpl;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;


/**
 * The test for the automated dummy activity.
 */
public class AutomatedDummyActivityTest {

    /** A temporary print stream. */
    private PrintStream tmp = null;

    /** The byte array output stream. */
    private final ByteArrayOutputStream out = new ByteArrayOutputStream();

    /** A dummy string. */
    private String dummyString = "I'm dumb";

    private BPMNToken token;
    
    private Navigator nav;

    /**
     * Set up.
     * @throws Exception
     *             the exception
     */
    @BeforeTest
    public void setUp()
    throws Exception {

        tmp = System.out;
        System.setOut(new PrintStream(out));
        
        nav = mock(Navigator.class);

        Node node = new NodeImpl(
            new AutomatedDummyActivity(dummyString),
            new SimpleJoinBehaviour(),
            new TakeAllSplitBehaviour());
        token = new BpmnTokenImpl(node, mock(BpmnProcessInstance.class), nav);
    }

//    /**
//     * Test activity initialization.
//     * The activity should not be null if it was instantiated correctly.
//     */
//    @Test
//    public void testActivityInitialization() {
//
//        assertNotNull(a, "It should not be null since it should be instantiated correctly");
//    }

    // @Test
    // public void testStateAfterActivityInitalization(){
    // assertEquals("It should have the state Initialized", State.INIT,
    // a.getState());
    // }

    /**
     * Test execute output.
     * If the activity is executed it should print out the given String.
     * @throws JodaEngineException 
     */
    @Test
    public void testExecuteOutput() throws JodaEngineException {

        token.executeStep();
        assertTrue(out.toString().indexOf(dummyString) != -1, "It should print out the given string when executed");
    }

    /**
     * Test state after execution.
     * After execution the activity should be in state TERMINTAED
     * @throws JodaEngineException 
     */
    @Test
    public void testStateAfterExecution() throws JodaEngineException {

        //a.execute(token);
        token.executeStep();
        assertEquals(token.getCurrentActivityState(), ActivityState.COMPLETED, "It should have the state Completed");
    }

    /**
     * Tear down.
     */
    @AfterClass
    public void tearDown() {

        System.setOut(tmp);
    }
}
