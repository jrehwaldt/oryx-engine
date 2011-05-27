package org.jodaengine.node.activity.custom;

import static org.testng.Assert.assertEquals;

import org.jodaengine.factory.node.AddNumbersAndStoreNodeFactory;
import org.jodaengine.process.instance.AbstractProcessInstance;
import org.jodaengine.process.instance.ProcessInstance;
import org.jodaengine.process.instance.ProcessInstanceContext;
import org.jodaengine.process.structure.Node;
import org.jodaengine.process.token.BpmnToken;
import org.jodaengine.process.token.Token;
import org.jodaengine.process.token.builder.BpmnTokenBuilder;
import org.mockito.Mockito;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;


/**
 * The Class AddNumbersAndStoreActivityTest. Tests for the AddNumberblablabla activity. Basically checks whether or not
 * the result is good.
 */
public class AddNumbersAndStoreActivityTest {

    /** The node. */
    private Node node = null;

    /** The p. */
    private Token p = null;

    /**
     * Sets up the environment with an addernode and a process token that shell execute one step.
     */
    @BeforeTest
    public void setUp() {

        AddNumbersAndStoreNodeFactory factory = new AddNumbersAndStoreNodeFactory();
        node = factory.create();
        AbstractProcessInstance instance = new ProcessInstance(null, Mockito.mock(BpmnTokenBuilder.class));
        p = new BpmnToken(node, instance, null);

    }

    /**
     * Test if the result of 1+1 is really 2 (see factory).
     * 
     * @throws Exception
     *             thrown if the execution of the node fails
     */
    @Test
    public void testResult()
    throws Exception {

        p.executeStep();
        ProcessInstanceContext context = p.getInstance().getContext();
        int i = Integer.parseInt((String) context.getVariable("result"));
        assertEquals(i, 2, "Upps we cant add correctly.");
    }
}
