package de.hpi.oryxengine.NavigatorTest;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import de.hpi.oryxengine.activity.impl.AutomatedDummyActivity;
import de.hpi.oryxengine.navigator.NavigatorImpl;
import de.hpi.oryxengine.processInstance.ProcessInstanceImpl;
import de.hpi.oryxengine.processstructure.NodeImpl;
import de.hpi.oryxengine.routingBehaviour.EmptyRoutingBehaviour;
import de.hpi.oryxengine.routingBehaviour.RoutingBehaviour;
import de.hpi.oryxengine.routingBehaviour.impl.TakeAllBehaviour;

/**
 * The test for the navigator.
 */
public class NavigatorTest {

    /** The navigator. */
    private NavigatorImpl navigator;

    /** Different nodes. */
    private NodeImpl node, node2;

    /** The process instance. */
    private ProcessInstanceImpl processInstance;

    /** The byte array output stream. */
    private final ByteArrayOutputStream out = new ByteArrayOutputStream();

    /** A temporary print stream. */
    private PrintStream tmp;

    /**
     * Set up. TODO: JavaDoc description of the test
     * 
     * @throws Exception
     *             the exception
     */
    @BeforeClass
    public void setUp()
    throws Exception {

        tmp = System.out;
        System.setOut(new PrintStream(out));

        navigator = new NavigatorImpl();
        navigator.start();

        RoutingBehaviour takeAllBehaviour = new TakeAllBehaviour();
        RoutingBehaviour emptyBehaviour = new EmptyRoutingBehaviour();
        AutomatedDummyActivity activity = new AutomatedDummyActivity("test");
        node = new NodeImpl(activity, takeAllBehaviour);
        node.setId("1");
        node2 = new NodeImpl(activity, emptyBehaviour);
        node2.setId("2");
        node.transitionTo(node2);
        processInstance = new ProcessInstanceImpl(node);

    }

    /**
    * Test signal length. 
    * TODO: more JavaScript
    */
    @Test
    public void testSignalLength() {

        navigator.startArbitraryInstance("1", processInstance);

        // this is not so nice, but I am not sure how to test correctly with parrallel behaviour
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Assert.assertEquals(processInstance.getCurrentNode(), node2);
        // assert processInstance.getCurrentNode().getId().equals("2");

    }

    // @Test
    // public void testSignalPrint(){
    // nav.startArbitraryInstance("1", processInstance);
    //
    // assert "test\ntest".equals(out.toString().trim());
    // }

    /**
     * Tear down.
     */
    @AfterClass
    public void tearDown() {

        System.setOut(tmp);
    }

}
