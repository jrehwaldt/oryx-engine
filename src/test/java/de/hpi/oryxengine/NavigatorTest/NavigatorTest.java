package de.hpi.oryxengine.NavigatorTest;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import de.hpi.oryxengine.activity.impl.AutomatedDummyActivity;
import de.hpi.oryxengine.navigator.impl.NavigatorImpl;
import de.hpi.oryxengine.processInstanceImpl.ProcessInstanceImpl;
import de.hpi.oryxengine.processstructure.impl.NodeImpl;
import de.hpi.oryxengine.routingBehaviour.RoutingBehaviour;
import de.hpi.oryxengine.routingBehaviour.impl.BPMNTakeAllBehaviour;
import de.hpi.oryxengine.routingBehaviour.impl.EmptyRoutingBehaviour;

public class NavigatorTest {

    NavigatorImpl nav;
    NodeImpl node, node2;
    ProcessInstanceImpl processInstance;
    private final ByteArrayOutputStream out = new ByteArrayOutputStream();
    private PrintStream tmp;

    @BeforeClass
    public void setUp()
    throws Exception {

        tmp = System.out;
        System.setOut(new PrintStream(out));

        nav = new NavigatorImpl();
        nav.start();

        RoutingBehaviour takeAllBehaviour = new BPMNTakeAllBehaviour();
        RoutingBehaviour emptyBehaviour = new EmptyRoutingBehaviour();
        AutomatedDummyActivity activity = new AutomatedDummyActivity("test");
        node = new NodeImpl(activity, takeAllBehaviour);
        node.setId("1");
        node2 = new NodeImpl(activity, emptyBehaviour);
        node2.setId("2");
        node.transitionTo(node2);
        processInstance = new ProcessInstanceImpl(node);

    }

    @Test
    public void testSignalLength() {

        nav.startArbitraryInstance("1", processInstance);

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

    @AfterClass
    public void tearDown() {

        System.setOut(tmp);
    }

}
