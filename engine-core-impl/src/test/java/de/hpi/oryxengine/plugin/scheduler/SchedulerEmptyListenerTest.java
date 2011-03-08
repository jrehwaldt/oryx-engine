package de.hpi.oryxengine.plugin.scheduler;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import de.hpi.oryxengine.factory.ExampleProcessTokenFactory;
import de.hpi.oryxengine.loadgenerator.LoadGenerator;
import de.hpi.oryxengine.navigator.schedule.FIFOScheduler;
import de.hpi.oryxengine.process.token.Token;

/**
 * Tests the SchedulerEmptyListener Plugin, that invokes a method on a loadgenerator when the queue of the Scheduler is.
 * empty
 */
public class SchedulerEmptyListenerTest {

    private FIFOScheduler scheduler;
    private LoadGenerator mockiGene;
    private Token pi;

    /**
     * Creates everything that we need (a Scheduler with the Plugin), a processtoken and a mocked loadGenerator.
     */
    @BeforeTest
    public void setUp() {

        scheduler = new FIFOScheduler();
        mockiGene = mock(LoadGenerator.class);
        scheduler.registerPlugin(SchedulerEmptyListener.getToken(mockiGene));
        ExampleProcessTokenFactory factory = new ExampleProcessTokenFactory();
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
