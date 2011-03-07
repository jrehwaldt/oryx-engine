package de.hpi.oryxengine.activity;

import static org.testng.Assert.*;

import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import de.hpi.oryxengine.factory.AddNumbersAndStoreNodeFactory;
import de.hpi.oryxengine.factory.SimpleProcessTokenFactory;
import de.hpi.oryxengine.process.structure.Node;
import de.hpi.oryxengine.process.token.Token;


/**
 * The Class AddNumbersAndStoreActivityTest.
 * Tests for the AddNumberblablabla activity. Basically checks whether or not the result is good.
 */
public class AddNumbersAndStoreActivityTest {
    private Node addernode;
    private Token p;
    
    /**
     * Sets up the environment with an addernode and a process token that shell execute one step.
     */
    @BeforeTest
    public void setUp() {
        AddNumbersAndStoreNodeFactory factory = new AddNumbersAndStoreNodeFactory();
        addernode = factory.create();
        SimpleProcessTokenFactory processfactory = new SimpleProcessTokenFactory(); 
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
