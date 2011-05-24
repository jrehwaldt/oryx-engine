package org.jodaengine.process.token;

import java.util.List;

import org.jodaengine.exception.JodaEngineException;
import org.jodaengine.navigator.Navigator;
import org.jodaengine.process.instance.AbstractProcessInstance;
import org.jodaengine.process.structure.Node;
import org.jodaengine.process.structure.Transition;

/**
 * The Class PetriTokenImpl.
 */
public class PetriTokenImpl extends AbstractToken<PetriTokenImpl> implements PetriToken  {
    
    /**
     * Instantiates a new petri token impl.
     *
     * @param start the start
     * @param instance the instance
     * @param nav the nav
     */
    public PetriTokenImpl(Node start, AbstractProcessInstance<PetriTokenImpl> instance, Navigator nav) {
        super(start, instance, nav);
    }
    

    /**
     * {@inheritDoc}
     */
    @Override
    public void executeStep()
    throws JodaEngineException {

        // TODO Auto-generated method stub
        
    }


    @Override
    public List<Token> navigateTo(List<Transition> transitionList) {

        // TODO Auto-generated method stub
        return null;
    }


    @Override
    public void cancelExecution() {

        // TODO Auto-generated method stub
        
    }

}
