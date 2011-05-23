package org.jodaengine.process.token;

import java.util.List;
import java.util.UUID;

import org.jodaengine.exception.JodaEngineException;
import org.jodaengine.ext.AbstractPluggable;
import org.jodaengine.ext.listener.AbstractTokenListener;
import org.jodaengine.navigator.Navigator;
import org.jodaengine.process.instance.AbstractProcessInstance;
import org.jodaengine.process.structure.Node;
import org.jodaengine.process.structure.Transition;

/**
 * The implementation of a process token.
 */
public class PetriTokenImpl extends AbstractPluggable<AbstractTokenListener> implements PetriToken {

    @Override
    public Node<PetriToken> getCurrentNode() {

        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void setCurrentNode(Node<PetriToken> node) {

        // TODO Auto-generated method stub
        
    }

    @Override
    public void executeStep()
    throws JodaEngineException {

        // TODO Auto-generated method stub
        
    }

    @Override
    public List<PetriToken> navigateTo(List<Transition<PetriToken>> transitionList) {

        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public PetriToken createNewToken(Node<PetriToken> n) {

        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public boolean joinable() {

        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public PetriToken performJoin() {

        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public AbstractProcessInstance<PetriToken> getInstance() {

        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Transition<PetriToken> getLastTakenTransition() {

        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void setLastTakenTransition(Transition<PetriToken> t) {

        // TODO Auto-generated method stub
        
    }

    @Override
    public Navigator getNavigator() {

        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void cancelExecution() {

        // TODO Auto-generated method stub
        
    }

    @Override
    public UUID getID() {

        // TODO Auto-generated method stub
        return null;
    }

}
