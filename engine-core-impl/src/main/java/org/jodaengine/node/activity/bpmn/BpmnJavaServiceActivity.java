package org.jodaengine.node.activity.bpmn;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.jodaengine.RepositoryService;
import org.jodaengine.exception.JodaEngineRuntimeException;
import org.jodaengine.node.activity.AbstractActivity;
import org.jodaengine.process.definition.ProcessDefinitionID;
import org.jodaengine.process.instance.ProcessInstanceContext;
import org.jodaengine.process.token.AbstractToken;

/**
 * Executes a custom java class. The custom class is searched for in the deployment scope.
 */
public class BpmnJavaServiceActivity extends AbstractActivity {

    private String fullClassName;

    /**
     * Instantiates a new scripting activity.
     *
     * @param fullClassName the full class name
     */
    public BpmnJavaServiceActivity(String fullClassName) {

        this.fullClassName = fullClassName;
    }

    @Override
    protected void executeIntern(AbstractToken token) {

        // get the class from the DeploymentScope of the definition
        ProcessDefinitionID definitionID = token.getInstance().getDefinition().getID();
        try {
            RepositoryService repoService = token.getRepositiory();
            // we expect this class to be a JodaScript (i.e. an implementation of it). No other classes can be used.
            Class<AbstractJavaTask> scriptClass = (Class<AbstractJavaTask>) repoService.getDeployedClass(definitionID,
                fullClassName);
            Method executeMethod = scriptClass.getMethod("execute", ProcessInstanceContext.class);
            executeMethod.invoke(null, token.getInstance().getContext());
        } catch (ClassNotFoundException e) {
            throw new JodaEngineRuntimeException("The script class does not exist in the process scope.", e);
        } catch (IllegalAccessException e) {
            throw new JodaEngineRuntimeException("The no-args constructor is not visible.", e);
        } catch (SecurityException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            throw new JodaEngineRuntimeException("The provided script-Class does not provide the execute method.", e);
        } catch (IllegalArgumentException e) {
            throw new JodaEngineRuntimeException("The provided script-Class does not accept the provided argument.", e);
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }

    }
    
    /**
     * This method is for testing purposes only.
     *
     * @return the service class name
     */
    public String getServiceClassName() {
        return fullClassName;
    }

}
