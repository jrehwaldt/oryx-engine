package org.jodaengine.node.activity.custom;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.jodaengine.RepositoryService;
import org.jodaengine.exception.JodaEngineRuntimeException;
import org.jodaengine.node.activity.AbstractActivity;
import org.jodaengine.process.definition.ProcessDefinitionID;
import org.jodaengine.process.instance.ProcessInstanceContext;
import org.jodaengine.process.token.BPMNToken;

/**
 * Executes a custom script. The custom script is searched for in the deployment scope.
 */
public class ScriptingActivity extends AbstractActivity {

    private String fullClassName;
    private RepositoryService repoService;

    /**
     * Instantiates a new scripting activity.
     *
     * @param fullClassName the full class name
     * @param repoService the repoService is used to look up the Script-Class.
     */
    public ScriptingActivity(String fullClassName, RepositoryService repoService) {

        this.fullClassName = fullClassName;
        this.repoService = repoService;
    }

    @Override
    protected void executeIntern(BPMNToken bPMNToken) {

        // get the class from the DeploymentScope of the definition
        ProcessDefinitionID definitionID = bPMNToken.getInstance().getDefinition().getID();
        try {
            // we expect this class to be a JodaScript (i.e. an implementation of it). No other classes can be used.
            Class<AbstractJodaScript> scriptClass = (Class<AbstractJodaScript>) this.repoService.getDeployedClass(definitionID,
                fullClassName);
            Method executeMethod = scriptClass.getMethod("execute", ProcessInstanceContext.class);
            executeMethod.invoke(null, bPMNToken.getInstance().getContext());
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

}
