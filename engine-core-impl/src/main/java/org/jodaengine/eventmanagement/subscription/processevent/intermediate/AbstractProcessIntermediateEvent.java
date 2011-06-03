package org.jodaengine.eventmanagement.subscription.processevent.intermediate;

import org.jodaengine.eventmanagement.adapter.EventType;
import org.jodaengine.eventmanagement.adapter.configuration.AdapterConfiguration;
import org.jodaengine.eventmanagement.subscription.ProcessEvent;
import org.jodaengine.eventmanagement.subscription.ProcessEventGroup;
import org.jodaengine.eventmanagement.subscription.ProcessIntermediateEvent;
import org.jodaengine.eventmanagement.subscription.condition.EventCondition;
import org.jodaengine.eventmanagement.subscription.condition.simple.TrueEventCondition;
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
     * Another builder for syntactical sugaring.
     * 
     * It only predefines a default {@link EventCondition} which is the {@link TrueEventCondition}.
     * 
     * @see AbstractProcessIntermediateEvent#AbstractProcessIntermediateEvent(EventType, AdapterConfiguration,
     *      EventCondition, Token)
     */
    protected AbstractProcessIntermediateEvent(EventType type, AdapterConfiguration config, Token token) {

        this(type, config, new TrueEventCondition(), token);
    }

    /**
     * Another builder for syntactical sugaring.
     * 
     * It only predefines a default {@link ProcessEventGroup}.
     * 
     * @see AbstractProcessIntermediateEvent#AbstractProcessIntermediateEvent(EventType, AdapterConfiguration,
     *      EventCondition, Token, ProcessEventGroup)
     */
    protected AbstractProcessIntermediateEvent(EventType type,
                                               AdapterConfiguration config,
                                               EventCondition condition,
                                               Token token) {

        this(type, config, condition, token, null);
    }

    /**
     * Default constructor.
     * 
     * @param type
     *            the type of this {@link ProcessEvent}
     * @param config
     *            - the {@link AdapterConfiguration configuration of the adapter} corresponding to this
     * @param condition
     *            - the conditions of this {@link ProcessEvent}
     * @param token
     *            - the {@link Token processToken}
     * @param parentEventGroup
     *            the {@link ProcessEventGroup} this {@link ProcessEvent} belongs to
     */
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
