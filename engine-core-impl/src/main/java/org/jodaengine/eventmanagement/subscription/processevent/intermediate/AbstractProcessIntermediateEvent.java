package org.jodaengine.eventmanagement.subscription.processevent.intermediate;

import org.jodaengine.eventmanagement.adapter.EventType;
import org.jodaengine.eventmanagement.adapter.configuration.AdapterConfiguration;
import org.jodaengine.eventmanagement.subscription.ProcessEvent;
import org.jodaengine.eventmanagement.subscription.ProcessEventGroup;
import org.jodaengine.eventmanagement.subscription.ProcessIntermediateEvent;
import org.jodaengine.eventmanagement.subscription.condition.EventCondition;
import org.jodaengine.eventmanagement.subscription.processevent.AbstractProcessEvent;
import org.jodaengine.process.structure.Node;
import org.jodaengine.process.token.Token;

/**
 * This is the base implementation for all {@link ProcessIntermediateEvent}s.
 */
public abstract class AbstractProcessIntermediateEvent extends AbstractProcessEvent implements ProcessIntermediateEvent {

    protected Token token;
    protected Node node;

    protected ProcessEventGroup parentEventGroup;

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
    protected AbstractProcessIntermediateEvent(EventType type,
                                               AdapterConfiguration config,
                                               EventCondition condition,
                                               Token token) {

        this(type, config, condition, token, null);
    }

    protected AbstractProcessIntermediateEvent(EventType type,
                                               AdapterConfiguration config,
                                               EventCondition condition,
                                               Token token,
                                               ProcessEventGroup parentEventGroup) {

        super(type, config, condition);
        this.token = token;
        this.node = token.getCurrentNode();
        this.parentEventGroup = parentEventGroup;
    }

    @Override
    public Token getToken() {

        return token;
    }

    @Override
    public void trigger() {

        if (parentEventGroup == null) {

            // Resuming the token with myself
            token.resume(this);
        } else {

            parentEventGroup.trigger(this);
        }

        // triggerIntern(); -> this can be abstract
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
