package org.jodaengine.process.instance;

import static org.mockito.Mockito.mock;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertTrue;

import org.jodaengine.navigator.Navigator;
import org.jodaengine.process.definition.ProcessDefinition;
import org.jodaengine.process.structure.Node;
import org.jodaengine.process.token.Token;
import org.jodaengine.process.token.TokenBuilder;
import org.jodaengine.process.token.builder.BpmnTokenBuilder;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;


/**
 * The Class ProcessInstanceImplTest.
 */
public class ProcessInstanceImplTest {

    private AbstractProcessInstance instance = null;
    private Token token;

    /**
     * Before class.
     */
    @BeforeClass
    public void setUp() {

        ProcessDefinition definition = mock(ProcessDefinition.class);
        Navigator nav = mock(Navigator.class);
        Node node = mock(Node.class);
        TokenBuilder builder = new BpmnTokenBuilder(nav, null);
        instance = new ProcessInstance(definition, builder);
        token = instance.createToken(node);

    }
    
    /**
     * F.
     */
    @Test
    public void testTokenCreation() {
        assertTrue(instance.getAssignedTokens().contains(token), "The instance should contain the token now.");
        assertNotNull(token.getInstance().getContext(), "The token should have a context to write to.");
    }

}
