package org.jodaengine.loadgenerator;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import java.util.List;

import org.jodaengine.NoRunningInstancesLoadgeneratorCaller;
import org.jodaengine.RepositoryService;
import org.jodaengine.ServiceFactory;
import org.jodaengine.exception.IllegalStarteventException;
import org.jodaengine.navigator.NavigatorImpl;
import org.jodaengine.process.definition.ProcessDefinition;
import org.jodaengine.process.instance.AbstractProcessInstance;
import org.jodaengine.process.structure.Node;
import org.jodaengine.process.token.BPMNToken;
import org.jodaengine.process.token.BPMNTokenImpl;
import org.jodaengine.repository.RepositorySetup;
import org.jodaengine.util.testing.AbstractJodaEngineTest;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

/**
 * Tests the SchedulerEmptyListener Plugin, that invokes a method on a loadgenerator when the queue of the Scheduler is.
 * empty
 */
public class NoRunningInstancesLoadgeneratorCallerTest extends AbstractJodaEngineTest {

    private NavigatorImpl nav = null;

    private LoadGenerator mockiGene = null;

    private BPMNToken pi = null;

    /**
     * Sets the up repo.
     * 
     * @throws IllegalStarteventException
     *             thrown if an illegal start event is given
     */
    @BeforeClass
    public void setUpRepo()
    throws IllegalStarteventException {

        RepositorySetup.fillRepository();
    }

    /**
     * Creates everything that we need (a Scheduler with the Plugin), a processtoken and a mocked loadGenerator.
     * 
     * @throws Exception
     *             the exception
     */
    @BeforeMethod
    public void setUp()
    throws Exception {

        nav = new NavigatorImpl();
        mockiGene = mock(LoadGenerator.class);
        NoRunningInstancesLoadgeneratorCaller caller = new NoRunningInstancesLoadgeneratorCaller(mockiGene);
        nav.registerPlugin(caller);
        RepositoryService repo = ServiceFactory.getRepositoryService();
        // TODO @Alle: Ist das hier wirklich gut (das mit der
        // RepositorySetup.FIRST_EXAMPLE_PROCESS_ID - ist ja ein Verweis auf
        // eine externe Lib? Tests sollten doch unabhängig sein
        ProcessDefinition def = repo.getProcessDefinition(RepositorySetup.getProcess1Plus1ProcessID());
        List<Node<BPMNToken>> startNodes = def.getStartNodes();
        Node<BPMNToken> startNode = startNodes.get(0);
        pi = new BPMNTokenImpl(startNode);
    }

    /**
     * Test that the method navigatorCurrentlyFinished() is invoked on load generator.
     */
    @Test
    public void testMethodInvokedOnLoadGenerator() {

        nav.startArbitraryInstance(pi);
        AbstractProcessInstance<BPMNToken> instance = pi.getInstance();
        nav.signalEndedProcessInstance(instance);

        // verify that the method we want is called on the load generator
        verify(mockiGene).navigatorCurrentlyFinished();

    }
}
