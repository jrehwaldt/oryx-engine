package org.jodaengine.eventmanagement.processevent.incoming.intermediate;

import javax.annotation.Nonnull;

import org.jodaengine.eventmanagement.adapter.EventType;
import org.jodaengine.eventmanagement.adapter.configuration.AdapterConfiguration;
import org.jodaengine.eventmanagement.processevent.incoming.AbstractIncomingProcessEvent;
import org.jodaengine.eventmanagement.processevent.incoming.TriggeringBehaviour;
import org.jodaengine.eventmanagement.processevent.incoming.condition.simple.TrueEventCondition;
import org.jodaengine.eventmanagement.processevent.incoming.intermediate.processeventgroup.DefaultTokenResumption;
import org.jodaengine.eventmanagement.subscription.IntermediateProcessEvent;
import org.jodaengine.eventmanagement.subscription.ProcessIntermediateEvent;
import org.jodaengine.eventmanagement.subscription.condition.EventCondition;
import org.jodaengine.process.structure.Node;
import org.jodaengine.process.token.Token;

/**
 * This is the base implementation for all {@link ProcessIntermediateEvent}s.
 */
public abstract class AbstractIncomingProcessIntermediateEvent extends AbstractIncomingProcessEvent 
    implements ProcessIntermediateEvent {

    protected Token token;
    protected Node node;

    protected TriggeringBehaviour parentEventGroup;

    /**
     * Another builder for syntactical sugaring.
     * 
     * It only predefines a default {@link EventCondition} which is the {@link TrueEventCondition}.
     * 
     * @param type
     *            the type of this {@link IntermediateProcessEvent}
     * @param config
     *            - the {@link AdapterConfiguration configuration of the adapter} corresponding to this
     * @param token
     *            - the {@link Token processToken}
     * 
     * @see AbstractIncomingProcessIntermediateEvent#AbstractProcessIntermediateEvent(EventType, AdapterConfiguration,
     *      EventCondition, Token)
     */
    protected AbstractIncomingProcessIntermediateEvent(EventType type, AdapterConfiguration config, Token token) {

        this(type, config, new TrueEventCondition(), token);
    }

    /**
     * Another builder for syntactical sugaring.
     * 
     * It only predefines a default {@link AbstractProcessIntermediateEventGroup}.
     * 
     * @param type
     *            the type of this {@link IntermediateProcessEvent}
     * @param config
     *            - the {@link AdapterConfiguration configuration of the adapter} corresponding to this
     * @param condition
     *            - the conditions of this {@link IntermediateProcessEvent}
     * @param token
     *            - the {@link Token processToken}F
     * 
     * @see AbstractIncomingProcessIntermediateEvent#AbstractProcessIntermediateEvent(EventType, AdapterConfiguration,
     *      EventCondition, Token, AbstractProcessIntermediateEventGroup)
     */
    protected AbstractIncomingProcessIntermediateEvent(EventType type,
                                               AdapterConfiguration config,
                                               EventCondition condition,
                                               Token token) {

        this(type, config, condition, token, new DefaultTokenResumption(token));
    }

    /**
     * Default constructor.
     * 
     * @param type
     *            the type of this {@link IntermediateProcessEvent}
     * @param config
     *            - the {@link AdapterConfiguration configuration of the adapter} corresponding to this
     * @param condition
     *            - the conditions of this {@link IntermediateProcessEvent}
     * @param token
     *            - the {@link Token processToken}
     * @param parentEventGroup
     *            the {@link AbstractProcessIntermediateEventGroup} this {@link IntermediateProcessEvent} belongs to
     */
    protected AbstractIncomingProcessIntermediateEvent(EventType type,
                                               AdapterConfiguration config,
                                               EventCondition condition,
                                               Token token,
                                               @Nonnull TriggeringBehaviour parentEventGroup) {

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
            parentEventGroup.trigger(this);
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
