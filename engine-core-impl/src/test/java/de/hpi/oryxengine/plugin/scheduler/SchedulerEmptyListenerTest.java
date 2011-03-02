package de.hpi.oryxengine.plugin.scheduler;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import de.hpi.oryxengine.factory.ExampleProcessInstanceFactory;
import de.hpi.oryxengine.loadgenerator.LoadGenerator;
import de.hpi.oryxengine.navigator.schedule.FIFOScheduler;
import de.hpi.oryxengine.process.instance.ProcessInstance;

/**
 * Tests the SchedulerEmptyListener Plugin, that invokes a method on a loadgenerator when the queue of the Scheduler is.
 * empty
 */
public class SchedulerEmptyListenerTest {

    private FIFOScheduler scheduler;
    private LoadGenerator mockiGene;
    private ProcessInstance pi;

    /**
     * Creates everything that we need (a Scheduler with the Plugin), a processinstance and a mocked loadGenerator.
     */
    @BeforeTest
    public void setUp() {

        scheduler = new FIFOScheduler();
        mockiGene = mock(LoadGenerator.class);
        scheduler.registerPlugin(SchedulerEmptyListener.getInstance(mockiGene));
        ExampleProcessInstanceFactory factory = new ExampleProcessInstanceFactory();
        pi = factory.create();
    }

    /**
     * Test that the method schedulerIsEmpty() is invoked on load generator.
     */
    @Test
    public void testMethodInvokedOnLoadGenerator() {

        scheduler.submit(pi);
        scheduler.retrieve();

        // verify that the method we want is called on the load generator
        verify(mockiGene).schedulerIsEmpty();

    }
}
