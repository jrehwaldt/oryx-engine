package org.jodaengine.process.token;

import org.jodaengine.navigator.Navigator;
import org.jodaengine.process.instance.AbstractProcessInstance;
import org.jodaengine.process.structure.Node;


/**
 * The Class AbstractPetriToken.
 */
public abstract class AbstractPetriToken extends AbstractToken<AbstractPetriToken> {
    
    /**
     * Instantiates a new abstract petri token.
     *
     * @param start the start
     * @param instance the instance
     * @param nav the nav
     */
    AbstractPetriToken(Node start, AbstractProcessInstance<AbstractPetriToken> instance, Navigator nav) {
            super(start, instance, nav);
    }


}
