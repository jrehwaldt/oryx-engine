package de.hpi.oryxengine.factory.process;

import de.hpi.oryxengine.ServiceFactory;
import de.hpi.oryxengine.exception.IllegalStarteventException;
import de.hpi.oryxengine.process.definition.ProcessBuilder;
import de.hpi.oryxengine.process.definition.ProcessDefinition;

/**
 * The Class AbstractProcessDeployer.
 */
public abstract class AbstractProcessDeployer implements ProcessDeployer {
    
    /** The builder. */
    protected ProcessBuilder builder;
    
    /**
     * Deploys the heavy computation process.
     *
     * @throws IllegalStarteventException the illegal startevent exception
     */
    public void deploy() throws IllegalStarteventException {
        this.createPseudoHuman();
        this.initializeNodes();
        ProcessDefinition definition = this.builder.buildDefinition();
        ServiceFactory.getDeplyomentService().deploy(definition);
    }
    
    /**
     * Initialize nodes.
     */
    abstract public void initializeNodes();
    
    /**
     * Creates a thread to complete human tasks. So human taks Process deployers shall overwrite this method.
     * Nothing happens here for automated task nodes, so they must not overwrite this method.
     */
    public void createPseudoHuman() {

    }

}
