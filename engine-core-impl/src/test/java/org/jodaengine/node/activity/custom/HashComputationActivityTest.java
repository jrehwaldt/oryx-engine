package org.jodaengine.node.activity.custom;

import static org.testng.Assert.assertEquals;

import org.jodaengine.factory.node.HashComputationNodeFactory;
import org.jodaengine.factory.token.SimpleProcessTokenFactory;
import org.jodaengine.process.instance.ProcessInstanceContext;
import org.jodaengine.process.structure.Node;
import org.jodaengine.process.token.Token;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;


/**
 * Tests the Hash computation Activity.
 */
public class HashComputationActivityTest {
  
    /** The hash node. */
    private Node hashNode = null;
    
    /** The p. */
    private Token p = null;
    
    /** The Constant TO_BE_HASHED. */
    private static final String TO_BE_HASHED = "Hello World!";
    
    /** The Constant VARIABLENAME. */
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
        SimpleProcessTokenFactory processFactory = new SimpleProcessTokenFactory();
        p = processFactory.create(hashNode);
    }
    
    /**
     * Test the hash.
     */
    @Test
    public void testTheHash() {
        try {
            p.executeStep();
        } catch (Exception e) {
            e.printStackTrace();
        }
        ProcessInstanceContext context = p.getInstance().getContext();
        assertEquals(context.getVariable(VARIABLENAME), SHA1, "Oh well hashing doesn't work what a shame.");
    }
}
