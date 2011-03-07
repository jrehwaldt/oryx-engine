package de.hpi.oryxengine.navigator.schedule;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

import java.util.LinkedList;
import java.util.List;

import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import de.hpi.oryxengine.factory.ExampleProcessInstanceFactory;
import de.hpi.oryxengine.process.instance.ProcessInstance;

/**
 * The Class FIFOSchedulerTest. tests our awesome FIFO Scheduler.
 */
public class FIFOSchedulerTest {

    private FIFOScheduler scheduler;
    private ProcessInstance firstInstance;
    private ProcessInstance secondInstance;

    /**
     * Before test.
     */
    @BeforeTest
    public void beforeTest() {

        scheduler = new FIFOScheduler();
        ExampleProcessInstanceFactory factory = new ExampleProcessInstanceFactory();
        firstInstance = factory.create();
        secondInstance = factory.create();
    }

    /**
     * Test submit and retrieve.
     */
    @Test
    public void testSubmitAndRetrieve() {

        scheduler.submit(firstInstance);
        assertEquals(scheduler.retrieve(), firstInstance,
            "We submitted something and immideatly retrieved it, but didn't get the same thing back.");
    }

    /**
     * Test we submit something, then it shouldn't be empty.
     */
    @Test
    public void testSubmitAndEmpty() {

        scheduler.submit(firstInstance);
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

        scheduler.submit(firstInstance);
        scheduler.submit(secondInstance);
        assertEquals(scheduler.retrieve(), firstInstance,
            "FIFO not working correctly, expected the that the first submitted gets retrieved");
    }
    
    /**
     * Test two submits and two retrieves.
     */
    @Test
    public void testTwoSubmitsAndTwoRetrieves() {

        scheduler.submit(firstInstance);
        scheduler.submit(secondInstance);
        scheduler.retrieve();
        assertEquals(scheduler.retrieve(), secondInstance,
            "FIFO not working correctly");
    }
    
    /**
     * Many tests for submit all, all in one place.
     */
    @Test
    public void testSubmitAll() {
        List<ProcessInstance> processList = new LinkedList<ProcessInstance>();
        processList.add(firstInstance);
        processList.add(secondInstance);
        scheduler.submitAll(processList);
        assertFalse(scheduler.isEmpty(), "Ohoh scheduler is emtpy after submitting a list of 2 process instances");
        assertEquals(scheduler.retrieve(), firstInstance, "Fifo not working with submit all");
        assertEquals(scheduler.retrieve(), secondInstance, "Fifo not working with submit all");
    }

}
