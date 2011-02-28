package de.hpi.oryxengine.activity;

import static org.testng.Assert.*;

import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import de.hpi.oryxengine.factory.AddNumbersAndStoreNodeFactory;
import de.hpi.oryxengine.factory.SimpleProcessInstanceFactory;
import de.hpi.oryxengine.process.instance.ProcessInstance;
import de.hpi.oryxengine.process.structure.Node;



/**
 * The Class AddNumbersAndStoreActivityTest.
 * Tests for the AddNumberblablabla activity. Basically schecks whether or not the result is good.
 */
public class AddNumbersAndStoreActivityTest {
    private Node addernode;
    private ProcessInstance p;
    
    /**
     * Sets up the environment with an addernode and a processinstance that shell execute one step.
     */
    @BeforeTest
    public void setUp() {
        AddNumbersAndStoreNodeFactory factory = new AddNumbersAndStoreNodeFactory();
        addernode = factory.create();
        SimpleProcessInstanceFactory processfactory = new SimpleProcessInstanceFactory(); 
        p = processfactory.create(addernode);
    }
    
    /**
     * Test if the result of 1+1 is really 2 (see factory).
     */
    @Test
    public void testResult() {
        p.executeStep();
        int i = Integer.parseInt((String) p.getVariable("result"));
        assertEquals(i, 2, "Upps we cant add correctly.");
    }
}
