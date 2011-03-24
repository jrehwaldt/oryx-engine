package de.hpi.oryxengine.process.instance;

import static org.mockito.Mockito.mock;
import org.testng.annotations.Test;
import org.testng.annotations.BeforeClass;
import static org.testng.Assert.assertTrue;
import static org.testng.Assert.assertNotNull;

import de.hpi.oryxengine.navigator.Navigator;
import de.hpi.oryxengine.process.definition.ProcessDefinition;
import de.hpi.oryxengine.process.structure.Node;
import de.hpi.oryxengine.process.token.Token;

/**
 * The Class ProcessInstanceImplTest.
 */
public class ProcessInstanceImplTest {

    private ProcessInstance instance;

    /**
     * F.
     */
    @Test
    public void testTokenCreation() {

        Navigator nav = mock(Navigator.class);
        Node node = mock(Node.class);
        Token token = instance.createToken(node, nav);

        assertTrue(instance.getTokens().contains(token), "The instance should contain the token now.");

        assertNotNull(token.getInstance().getContext(), "The token should have a context to write to.");
    }

    /**
     * Before class.
     */
    @BeforeClass
    public void setUp() {

        ProcessDefinition definition = mock(ProcessDefinition.class);
        instance = new ProcessInstanceImpl(definition);

    }

}
