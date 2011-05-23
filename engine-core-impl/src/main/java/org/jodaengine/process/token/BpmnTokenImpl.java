package org.jodaengine.process.token;

import org.jodaengine.exception.NoValidPathException;
import org.jodaengine.navigator.Navigator;
import org.jodaengine.node.activity.ActivityState;
import org.jodaengine.process.instance.AbstractProcessInstance;
import org.jodaengine.process.structure.Node;

/**
 * 
 * @author Gery
 * 
 */
public class BpmnTokenImpl extends AbstractToken<BpmnTokenImpl> implements BPMNToken {

    private ActivityState currentActivityState = null;
    
    public BpmnTokenImpl(Node startNode, AbstractProcessInstance instance, Navigator navigator) {

        super(startNode, instance, navigator);
        changeActivityState(ActivityState.INIT);
    }

    @Override
    public ActivityState getCurrentActivityState() {

        return currentActivityState;
    }

    @Override
    public void suspend() {

        changeActivityState(ActivityState.WAITING);
        navigator.addSuspendToken(this);
    }

    @Override
    public void resume() {

        navigator.removeSuspendToken(this);

        try {
            completeExecution();
        } catch (NoValidPathException nvpe) {
            exceptionHandler.processException(nvpe, this);
        }
    }

}
