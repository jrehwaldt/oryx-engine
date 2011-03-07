package de.hpi.oryxengine.navigator;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.UUID;

import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import de.hpi.oryxengine.activity.impl.AutomatedDummyActivity;
import de.hpi.oryxengine.process.instance.ProcessInstanceImpl;
import de.hpi.oryxengine.process.structure.NodeImpl;
import de.hpi.oryxengine.routing.behaviour.RoutingBehaviour;
import de.hpi.oryxengine.routing.behaviour.impl.EmptyRoutingBehaviour;
import de.hpi.oryxengine.routing.behaviour.impl.TakeAllBehaviour;

/**
 * The test for the navigator.
 */
public class NavigatorTest {

    /** The Constant SLEEP_TIME. */
    private static final int SLEEP_TIME = 3000;

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
     * Set up.
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
        node2 = new NodeImpl(activity, emptyBehaviour);
        node.transitionTo(node2);
        processInstance = new ProcessInstanceImpl(node);

    }

    /**
    * Test signal length. 
    * TODO more JavaScript
    */
    @Test
    public void testSignalLength() {
        
        navigator.startArbitraryInstance(UUID.randomUUID(), processInstance);

        // this is not so nice, but I am not sure how to test correctly with parrallel behaviour
        try {
            Thread.sleep(SLEEP_TIME);
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
