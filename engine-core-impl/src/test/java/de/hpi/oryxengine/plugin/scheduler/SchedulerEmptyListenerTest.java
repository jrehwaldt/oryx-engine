package de.hpi.oryxengine.plugin.scheduler;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import java.util.List;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import de.hpi.oryxengine.ServiceFactory;
import de.hpi.oryxengine.loadgenerator.LoadGenerator;
import de.hpi.oryxengine.navigator.schedule.FIFOScheduler;
import de.hpi.oryxengine.process.definition.ProcessDefinition;
import de.hpi.oryxengine.process.structure.Node;
import de.hpi.oryxengine.process.structure.StartNode;
import de.hpi.oryxengine.process.token.Token;
import de.hpi.oryxengine.process.token.TokenImpl;
import de.hpi.oryxengine.repository.ProcessRepository;
import de.hpi.oryxengine.repository.ProcessRepositoryImpl;
import de.hpi.oryxengine.repository.RepositorySetup;

/**
 * Tests the SchedulerEmptyListener Plugin, that invokes a method on a loadgenerator when the queue of the Scheduler is.
 * empty
 */
public class SchedulerEmptyListenerTest {

    /** The scheduler. */
    private FIFOScheduler scheduler;
    
    /** The mocki gene. */
    private LoadGenerator mockiGene;
    
    /** The pi. */
    private Token pi;

    /**
     * Sets the up repo.
     */
    @BeforeClass
    public void setUpRepo() {

        RepositorySetup.fillRepository();
    }

    /**
     * Creates everything that we need (a Scheduler with the Plugin), a processtoken and a mocked loadGenerator.
     *
     * @throws Exception the exception
     */
    @BeforeMethod
    public void setUp() throws Exception {

        scheduler = new FIFOScheduler();
        mockiGene = mock(LoadGenerator.class);
        SchedulerEmptyListener listener = new SchedulerEmptyListener(mockiGene);
        scheduler.registerPlugin(listener);
        ProcessRepository repo = ServiceFactory.getRepositoryService();
        ProcessDefinition def = repo.getDefinition(RepositorySetup.FIRST_EXAMPLE_PROCESS_ID);
        List<StartNode> startNodes = def.getStartNodes();
        Node startNode = startNodes.get(0);
        pi = new TokenImpl(startNode);
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
