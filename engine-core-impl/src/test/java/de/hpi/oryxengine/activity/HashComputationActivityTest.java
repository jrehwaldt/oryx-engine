package de.hpi.oryxengine.activity;

import static org.testng.Assert.assertEquals;

import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import de.hpi.oryxengine.factory.HashComputationNodeFactory;
import de.hpi.oryxengine.factory.SimpleProcessInstanceFactory;
import de.hpi.oryxengine.process.instance.ProcessInstance;
import de.hpi.oryxengine.process.structure.Node;

/**
 * Tests the Hashcomputation Activity.
 */
public class HashComputationActivityTest {
  
    private Node hashNode;
    private ProcessInstance p;
    private static final String TO_BE_HASHED = "Hello World!";
    private static final String VARIABLENAME = "result";
    
    
    /** The SHA1 hash of "Hello World!" as computed by http://www.fileformat.info/tool/hash.htm?text=Hello+World! */
    private static final String SHA1 = "2ef7bde608ce5404e97d5f042f95f89f1c232871";
    
    /**
     * Sets the up.
     */
    @BeforeTest
    public void setUp() {
        HashComputationNodeFactory factory = new HashComputationNodeFactory(VARIABLENAME, TO_BE_HASHED);
        hashNode = factory.create();
        SimpleProcessInstanceFactory processFactory = new SimpleProcessInstanceFactory();
        p = processFactory.create(hashNode);
    }
    
    /**
     * Test the hash.
     */
    @Test
    public void testTheHash() {
        p.executeStep();
        assertEquals(p.getVariable(VARIABLENAME), SHA1, "Oh well hashing doesn't work what a shame.");
    }
}
