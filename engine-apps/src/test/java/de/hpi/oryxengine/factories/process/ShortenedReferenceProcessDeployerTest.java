package de.hpi.oryxengine.factories.process;

import static org.testng.Assert.assertEquals;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import de.hpi.oryxengine.NavigatorImplMock;
import de.hpi.oryxengine.ServiceFactory;
import de.hpi.oryxengine.exception.DefinitionNotFoundException;
import de.hpi.oryxengine.exception.IllegalStarteventException;
import de.hpi.oryxengine.navigator.Navigator;
import de.hpi.oryxengine.process.instance.AbstractProcessInstance;
import de.hpi.oryxengine.process.instance.ProcessInstanceImpl;
import de.hpi.oryxengine.process.token.Token;
import de.hpi.oryxengine.process.token.TokenImpl;

/**
 * The Class ShortenedReferenceProcessDeployerTest. This is the test class for {@link ShortenedReferenceProcessDeployer}
 * .
 */
public class ShortenedReferenceProcessDeployerTest extends AbstractProcessDeployerTest {

    private Token token;
    private AbstractProcessInstance processInstance = null;
    private ShortenedReferenceProcessDeployer instanceDefinition;

    @BeforeMethod
    @Override
    public void setUp()
    throws IllegalStarteventException {

        instanceDefinition = new ShortenedReferenceProcessDeployer();
        this.uuid = instanceDefinition.deploy();
        try {
            processInstance = new ProcessInstanceImpl(ServiceFactory.getRepositoryService().getProcessDefinition(uuid));
        } catch (DefinitionNotFoundException e) {
            System.out.println("Definition nicht gefunden! ");
            e.printStackTrace();
        }
        Navigator navigator = new NavigatorImplMock();
        token = new TokenImpl(processInstance.getDefinition().getStartNodes().get(0), processInstance, navigator);
    }

    /**
     * Test if process runs correctly.
     */
    @Test
    public void testProcessRuns() {

        assertEquals(token.getCurrentNode(), instanceDefinition.getStartNode());
    }

}
