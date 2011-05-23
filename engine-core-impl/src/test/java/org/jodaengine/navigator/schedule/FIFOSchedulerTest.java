package org.jodaengine.navigator.schedule;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

import java.util.LinkedList;
import java.util.List;

import org.jodaengine.RepositoryService;
import org.jodaengine.ServiceFactory;
import org.jodaengine.exception.IllegalStarteventException;
import org.jodaengine.process.definition.ProcessDefinition;
import org.jodaengine.process.instance.ProcessInstanceImpl;
import org.jodaengine.process.structure.Node;
import org.jodaengine.process.token.BPMNToken;
import org.jodaengine.process.token.BPMNTokenImpl;
import org.jodaengine.repository.RepositorySetup;
import org.jodaengine.util.testing.AbstractJodaEngineTest;
import org.jodaengine.util.testing.SkipBuildingJodaEngine;
import org.jodaengine.util.testing.SkipBuildingJodaEngine.JodaEngineTestSkipMode;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;


/**
 * The Class FIFOSchedulerTest. tests our awesome FIFO Scheduler.
 */
@SkipBuildingJodaEngine(skippingMode = JodaEngineTestSkipMode.FOR_EACH_TEST_METHOD)
public class FIFOSchedulerTest extends AbstractJodaEngineTest {

    /** The scheduler. */
    private FIFOScheduler scheduler = null;
    
    /** The first token. */
    private BPMNToken firstToken = null;
    
    /** The second token. */
    private BPMNToken secondToken = null;

    
    /**
     * Sets the up repo.
     * @throws IllegalStarteventException 
     */
    @BeforeClass
    public void setUpRepo() throws IllegalStarteventException {

        RepositorySetup.fillRepository();
    }

    /**
     * Before test.
     *
     * @throws Exception the exception
     */
    @BeforeMethod
    public void beforeTest()
    throws Exception {

        scheduler = new FIFOScheduler();
        RepositoryService repo = ServiceFactory.getRepositoryService();
        ProcessDefinition def = repo.getProcessDefinition(RepositorySetup.getProcess1Plus1ProcessID());
        List<Node<BPMNToken>> startNodes = def.getStartNodes();
        Node<BPMNToken> startNode = startNodes.get(0);
        firstToken = new BPMNTokenImpl(startNode, new ProcessInstanceImpl(null), null);
        secondToken = new BPMNTokenImpl(startNode, new ProcessInstanceImpl(null), null);
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
        assertEquals(scheduler.retrieve(), firstToken,
            "FIFO not working correctly, expected the that the first submitted gets retrieved");
    }

    /**
     * Test two submits and two retrieves.
     */
    @Test
    public void testTwoSubmitsAndTwoRetrieves() {

        scheduler.submit(firstToken);
        scheduler.submit(secondToken);
        scheduler.retrieve();
        assertEquals(scheduler.retrieve(), secondToken, "FIFO not working correctly");
    }

    /**
     * Many tests for submit all, all in one place.
     */
    @Test
    public void testSubmitAll() {

        List<BPMNToken> processList = new LinkedList<BPMNToken>();
        processList.add(firstToken);
        processList.add(secondToken);
        scheduler.submitAll(processList);
        assertFalse(scheduler.isEmpty(), "Ohoh scheduler is emtpy after submitting a list of 2 process instances");
        assertEquals(scheduler.retrieve(), firstToken, "Fifo not working with submit all");
        assertEquals(scheduler.retrieve(), secondToken, "Fifo not working with submit all");
    }

}
