package org.jodaengine.node.activity.custom;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.jodaengine.RepositoryServiceInside;
import org.jodaengine.node.activity.Activity;
import org.jodaengine.node.activity.ContextVariableJavaTask;
import org.jodaengine.node.activity.bpmn.BpmnJavaServiceActivity;
import org.jodaengine.process.definition.ProcessDefinition;
import org.jodaengine.process.instance.AbstractProcessInstance;
import org.jodaengine.process.instance.ProcessInstanceContext;
import org.jodaengine.process.instance.ProcessInstanceContextImpl;
import org.jodaengine.process.token.AbstractToken;
import org.jodaengine.util.mock.MockUtils;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

/**
 * The Class ScriptingActivityTest.
 */
public class JavaServiceActivityTest {
    
    private AbstractToken token = null;
    private RepositoryServiceInside repoMock = null;
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

        repoMock = mock(RepositoryServiceInside.class);

        // as soon as we infer generic type arguments her (<?>), we get compile errors with #thenReturn()?!
        Class clazz = ContextVariableJavaTask.class;
        when(token.getRepositiory()).thenReturn(repoMock);
        when(repoMock.getDeployedClass(mockDefinition.getID(), "org.jodaengine.node.activity.ContextVariableJavaTask"))
            .thenReturn(clazz);
    }
    
    /**
     * Test the execution of the {@link ContextVariableJavaTask}.
     *
     * @throws ClassNotFoundException the class not found exception
     */
    @Test
    public void testScriptExecution() throws ClassNotFoundException {


        Activity activity = new BpmnJavaServiceActivity("org.jodaengine.node.activity.ContextVariableJavaTask");
        activity.execute(token);

        Assert.assertEquals(context.getVariable("serviceVariable"), "set",
            "The variable scriptVariable should be set to 'set'");
    }
}
