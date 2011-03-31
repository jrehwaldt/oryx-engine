package de.hpi.oryxengine.navigator;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import de.hpi.oryxengine.factory.node.RoutingBehaviourTestFactory;
import de.hpi.oryxengine.process.instance.ProcessInstanceImpl;
import de.hpi.oryxengine.process.structure.Node;
import de.hpi.oryxengine.process.token.TokenImpl;

/**
 * The test for the navigator.
 */
public class NavigatorTest {

    /** The Constant SLEEP_TIME. */
    private static final int SLEEP_TIME = 3000;

    /** The navigator. */
    private NavigatorImpl navigator = null;

    /** Different nodes. */
    private Node node = null, node2 = null;

    /** The process instance. */
    private TokenImpl processToken = null;

    /** The byte array output stream. */
    private final ByteArrayOutputStream out = new ByteArrayOutputStream();

    /** A temporary print stream. */
    private PrintStream tmp = null;

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

        node = new RoutingBehaviourTestFactory().createWithAndSplit();
        node2 = new RoutingBehaviourTestFactory().createWithAndSplit();
        node.transitionTo(node2);
        processToken = new TokenImpl(node, new ProcessInstanceImpl(null), navigator);

    }

    /**
     * Test signal length.
     * 
     */
    @Test
    public void testSignalLength() {

        navigator.startArbitraryInstance(processToken);

        // this is not so nice, but I am not sure how to test correctly with parrallel behaviour
        try {
            Thread.sleep(SLEEP_TIME);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Assert.assertEquals(processToken.getCurrentNode(), node2);
    }

    /**
     * Tear down.
     */
    @AfterClass
    public void tearDown() {

        System.setOut(tmp);
    }

}
