package de.hpi.oryxengine.node.activity;

import static org.testng.Assert.assertEquals;

import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import de.hpi.oryxengine.factory.node.AddNumbersAndStoreNodeFactory;
import de.hpi.oryxengine.process.instance.AbstractProcessInstance;
import de.hpi.oryxengine.process.instance.ProcessInstanceContext;
import de.hpi.oryxengine.process.instance.ProcessInstanceImpl;
import de.hpi.oryxengine.process.structure.Node;
import de.hpi.oryxengine.process.token.Token;
import de.hpi.oryxengine.process.token.TokenImpl;

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
        AbstractProcessInstance instance = new ProcessInstanceImpl(null);
        p = new TokenImpl(node, instance);

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
