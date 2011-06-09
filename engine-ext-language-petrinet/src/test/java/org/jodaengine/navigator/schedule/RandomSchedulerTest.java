package org.jodaengine.navigator.schedule;


import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

import java.util.LinkedList;
import java.util.List;

import org.jodaengine.node.factory.petri.PetriNodeFactory;
import org.jodaengine.process.instance.AbstractProcessInstance;
import org.jodaengine.process.instance.ProcessInstance;
import org.jodaengine.process.structure.Node;
import org.jodaengine.process.token.Token;
import org.jodaengine.process.token.TokenBuilder;
import org.jodaengine.process.token.builder.PetriTokenBuilder;
import org.jodaengine.util.testing.AbstractJodaEngineTest;
import org.jodaengine.util.testing.SkipBuildingJodaEngine;
import org.jodaengine.util.testing.SkipBuildingJodaEngine.JodaEngineTestSkipMode;
import org.mockito.Mockito;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;


/**
 * The Class RandomSchedulerTest.
 */
@SkipBuildingJodaEngine(skippingMode = JodaEngineTestSkipMode.FOR_EACH_TEST_METHOD)
public class RandomSchedulerTest extends AbstractJodaEngineTest {

    /** The scheduler. */
    private RandomPetriNetScheduler scheduler = null;
    
    /** The first token. */
    private Token firstToken = null;
    
    /** The second token. */
    private Token secondToken = null;
    
    private AbstractProcessInstance instance;

    /**
     * Before test.
     *
     * @throws Exception the exception
     */
    @BeforeMethod
    public void beforeTest()
    throws Exception {

        scheduler = new RandomPetriNetScheduler();
        Node startNode = PetriNodeFactory.createPlace();
        TokenBuilder builder = new PetriTokenBuilder(null, null);
        instance = new ProcessInstance(null, builder);
        firstToken = instance.createToken(startNode);
        secondToken = instance.createToken(startNode);
    }
    
    /**
     * After test.
     */
    @AfterMethod
    public void afterTest() {
        scheduler = null;
        firstToken = null;
        secondToken = null;
        instance = null;
    }

    /**
     * Test submit and retrieve.
     */
    @Test
    public void testSubmitAndRetrieve() {

        scheduler.submit(firstToken);
        assertEquals(scheduler.retrieve(), firstToken,
            "We submitted something and immideatly retrieved it, but didn't get the same thing back.");
    }

    /**
     * Test we submit something, then it shouldn't be empty.
     */
    @Test
    public void testSubmitAndEmpty() {

        scheduler.submit(firstToken);
        assertFalse(scheduler.isEmpty(), "Scheduler is empty after something got submitted.");

    }
    
    /**
     * Anti Concurrency Behaviour, positive situation: After one retrieval of a token, the scheduler should be locked.
     * When a new token is added, it can continue execution.
     * This results in a single threaded solution for an instance, which is important due to petriNet definition.
     * There can be multiple working threads, if there are multiple instances.
     */
    @Test
    public void testSequentialRetrieval() {

        scheduler.submit(firstToken);
        Token goodToken = scheduler.retrieve();
        scheduler.submit(secondToken);
        Token anotherGoodToken = scheduler.retrieve();
        assertEquals(goodToken, firstToken, "Scheduler is empty after something got submitted.");
        assertEquals(anotherGoodToken, secondToken, "Scheduler is empty after something got submitted.");

    }
    
    /**
     * Anti Concurrency Behaviour, positive situation: After one retrieval of a token, the scheduler should be locked.
     * When a new token is added, it can continue execution.
     * This results in a single threaded solution for an instance, which is important due to petriNet definition.
     * There can be multiple working threads, if there are multiple instances.
     */
    @Test
    public void testParallelRetrieval() {

        scheduler.submit(firstToken);
        scheduler.submit(secondToken);
        Token goodToken = scheduler.retrieve();
        Token badToken = scheduler.retrieve();
        
        assertTrue(goodToken instanceof Token, "Scheduler is empty after something got submitted.");
        assertEquals(badToken, null, "Scheduler is empty after something got submitted.");

    }
    

    /**
     * Test that the scheduler is initially empty.
     */
    @Test
    public void testInitiallyEmpty() {

        assertTrue(scheduler.isEmpty(), "Scheduler isn't initally empty.");
    }

    /**
     * Test two submits and retrieve.
     */
    @Test
    public void testTwoSubmitsAndRetrieve() {

        scheduler.submit(firstToken);
        scheduler.submit(secondToken);
        scheduler.retrieve();
        assertEquals(scheduler.size(), 1,
            "Random not working correctly, expected that exactly one token gets retrieved");
    }

    /**
     * Test two submits and two retrieves.
     */
    @Test
    public void testTwoSubmitsAndTwoRetrieves() {

        scheduler.submit(firstToken);
        scheduler.submit(secondToken);
        Token chosenToken = scheduler.retrieve();
        //Token 1 is finished
        scheduler.releaseLock(chosenToken);
        scheduler.retrieve();
        assertEquals(scheduler.size(), 0,
        "Random not working correctly, expected that exactly two token get retrieved");
    }

    /**
     * Many tests for submit all, all in one place.
     */
    @Test
    public void testSubmitAll() {

        List<Token> processList = new LinkedList<Token>();
        processList.add(firstToken);
        processList.add(secondToken);
        scheduler.submitAll(processList);
        assertFalse(scheduler.isEmpty(), "Ohoh scheduler is emtpy after submitting a list of 2 process instances");
        Token chosenToken = scheduler.retrieve();
        //Token 1 is finished
        scheduler.releaseLock(chosenToken);
        scheduler.retrieve();
        assertEquals(scheduler.size(), 0,
        "Random not working correctly, expected that exactly two token get retrieved");
    }
    
    

}
