package org.jodaengine.node.activity.custom;

import org.jodaengine.RepositoryService;
import org.jodaengine.exception.JodaEngineRuntimeException;
import org.jodaengine.node.activity.AbstractActivity;
import org.jodaengine.process.definition.ProcessDefinitionID;
import org.jodaengine.process.token.Token;

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
    protected void executeIntern(Token token) {

        // get the class from the DeploymentScope of the definition
        ProcessDefinitionID definitionID = token.getInstance().getDefinition().getID();
        try {
            Class<JodaScript> scriptClass = (Class<JodaScript>) this.repoService.getDeployedClass(definitionID,
                fullClassName);
            JodaScript instance = scriptClass.newInstance();
            instance.execute(token.getInstance().getContext());
        } catch (ClassNotFoundException e) {
            throw new JodaEngineRuntimeException("The script class does not exist in the process scope.", e);
        } catch (InstantiationException e) {
            throw new JodaEngineRuntimeException("The script class does not have a no-argmuents constructor.", e);
        } catch (IllegalAccessException e) {
            throw new JodaEngineRuntimeException("The no-args constructor is not visible.", e);
        }

    }

}
