package de.hpi.oryxengine.activity;

import static org.testng.Assert.*;

import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import de.hpi.oryxengine.factory.node.AddNumbersAndStoreNodeFactory;
import de.hpi.oryxengine.factory.token.SimpleProcessTokenFactory;
import de.hpi.oryxengine.process.structure.Node;
import de.hpi.oryxengine.process.token.Token;


/**
 * The Class AddNumbersAndStoreActivityTest.
 * Tests for the AddNumberblablabla activity. Basically checks whether or not the result is good.
 */
public class AddNumbersAndStoreActivityTest {
    private Node node = null;
    private Token p = null;
    
    /**
     * Sets up the environment with an addernode and a process token that shell execute one step.
     */
    @BeforeTest
    public void setUp() {
        AddNumbersAndStoreNodeFactory factory = new AddNumbersAndStoreNodeFactory();
        node = factory.create();
        SimpleProcessTokenFactory processfactory = new SimpleProcessTokenFactory(); 
        p = processfactory.create(node);
    }
    
    /**
     * Test if the result of 1+1 is really 2 (see factory).
     * @throws Exception thrown if the execution of the node fails
     */
    @Test
    public void testResult() throws Exception {
        p.executeStep();
        int i = Integer.parseInt((String) p.getContext().getVariable("result"));
        assertEquals(i, 2, "Upps we cant add correctly.");
    }
}
