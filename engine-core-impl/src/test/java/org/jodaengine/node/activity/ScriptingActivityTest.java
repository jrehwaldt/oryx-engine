package org.jodaengine.node.activity;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.jodaengine.RepositoryService;
import org.jodaengine.node.activity.custom.ScriptingActivity;
import org.jodaengine.process.definition.ProcessDefinition;
import org.jodaengine.process.instance.AbstractProcessInstance;
import org.jodaengine.process.instance.ProcessInstanceContext;
import org.jodaengine.process.instance.ProcessInstanceContextImpl;
import org.jodaengine.process.token.Token;
import org.jodaengine.util.mock.MockUtils;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

/**
 * The Class ScriptingActivityTest.
 */
public class ScriptingActivityTest {
    
    private Token token = null;
    private RepositoryService repoMock = null;
    private ProcessInstanceContext context = null;

    /**
     * Sets up mocks for RepositoryService, creates an instance context, etc.
     *
     * @throws ClassNotFoundException the class not found exception
     */
    @BeforeClass
    public void setUp() throws ClassNotFoundException {
        token = MockUtils.fullyMockedToken();
        ProcessDefinition mockDefinition = token.getInstance().getDefinition();
        AbstractProcessInstance mockInstance = token.getInstance();

        context = new ProcessInstanceContextImpl();
        when(mockInstance.getContext()).thenReturn(context);

        repoMock = mock(RepositoryService.class);

        // as soon as we infer generic type arguments her (<?>), we get compile errors with #thenReturn()?!
        Class clazz = ContextVariableScript.class;
        when(repoMock.getDeployedClass(mockDefinition.getID(), "org.jodaengine.node.activity.ContextVariableScript"))
            .thenReturn(clazz);
    }
    
    /**
     * Test the execution of the {@link ContextVariableScript}.
     *
     * @throws ClassNotFoundException the class not found exception
     */
    @Test
    public void testScriptExecution() throws ClassNotFoundException {


        Activity activity = new ScriptingActivity("org.jodaengine.node.activity.ContextVariableScript", repoMock);
        activity.execute(token);

        Assert.assertEquals(context.getVariable("scriptVariable"), "set",
            "The variable scriptVariable should be set to 'set'");
    }
}
