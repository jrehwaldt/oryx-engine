package org.jodaengine.eventmanagement.subscription;

import org.jodaengine.eventmanagement.adapter.EventType;
import org.jodaengine.eventmanagement.adapter.configuration.AdapterConfiguration;
import org.jodaengine.eventmanagement.subscription.condition.EventCondition;
import org.jodaengine.process.structure.Node;
import org.jodaengine.process.token.Token;

/**
 * This is the base implementation for all {@link ProcessIntermediateEvent}s.
 */
public class ProcessIntermediateEventBase extends AbstractProcessEvent implements ProcessIntermediateEvent {

    protected Token token;
    protected Node node;

    /**
     * Default constructor.
     * 
     * @param type
     *            the type of this {@link ProcessEvent}
     * @param config
     *            - the {@link AdapterConfiguration configuration of the adapter} corresponding to this
     *            {@link ProcessEvent}
     * @param condition
     *            - the conditions of this {@link ProcessEvent}
     * @param token
     *            - the {@link Token processToken}
     */
    protected ProcessIntermediateEventBase(EventType type,
                                           AdapterConfiguration config,
                                           EventCondition condition,
                                           Token token) {

        super(type, config, condition);
        this.token = token;
        this.node = token.getCurrentNode();
    }

    @Override
    public Token getToken() {

        return token;
    }

    @Override
    public void trigger() {

        // TODO hier kommt das hin mit der EventGruppe
        // Bsp. if (called) {
        // return;
        // }
        // triggerIntern(); -> this can be abstract
        // Resuming the token with myself
        token.resume(this);
    }

    @Override
    public Node getFireringNode() {

        return node;
    }

    @Override
    public void setFireringNode(Node node) {

        this.node = node;
    }

}
