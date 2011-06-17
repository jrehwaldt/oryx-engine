package org.jodaengine.eventmanagement.processevent.incoming.intermediate;

import javax.annotation.Nonnull;

import org.jodaengine.eventmanagement.adapter.EventType;
import org.jodaengine.eventmanagement.adapter.configuration.AdapterConfiguration;
import org.jodaengine.eventmanagement.processevent.incoming.AbstractIncomingProcessEvent;
import org.jodaengine.eventmanagement.processevent.incoming.TriggeringBehaviour;
import org.jodaengine.eventmanagement.processevent.incoming.condition.simple.TrueEventCondition;
import org.jodaengine.eventmanagement.processevent.incoming.intermediate.processeventgroup.AbstractIntermediateProcessEventGroup;
import org.jodaengine.eventmanagement.processevent.incoming.intermediate.processeventgroup.DefaultTokenResumption;
import org.jodaengine.eventmanagement.subscription.IncomingProcessEvent;
import org.jodaengine.eventmanagement.subscription.condition.EventCondition;
import org.jodaengine.process.structure.Node;
import org.jodaengine.process.token.Token;

/**
 * This is the base implementation for all {@link IncomingIntermediateProcessEvent}s.
 */
public abstract class AbstractIncomingIntermediateProcessEvent extends AbstractIncomingProcessEvent 
    implements IncomingIntermediateProcessEvent {

    protected Token token;
    protected Node node;

    protected TriggeringBehaviour triggeringBehavior;

    /**
     * Another builder for syntactical sugaring.
     * 
     * It only predefines a default {@link EventCondition} which is the {@link TrueEventCondition}.
     * @param config
     *            - the {@link AdapterConfiguration configuration of the adapter} corresponding to this
     * @param token
     *            - the {@link Token processToken}
     * 
     * @see AbstractIncomingIntermediateProcessEvent#AbstractProcessIntermediateEvent(EventType, AdapterConfiguration,
     *      EventCondition, Token)
     */
    protected AbstractIncomingIntermediateProcessEvent(AdapterConfiguration config, Token token) {

        this(config, new TrueEventCondition(), token);
    }

    /**
     * Default constructor.
     * @param config
     *            - the {@link AdapterConfiguration configuration of the adapter} corresponding to this
     * @param condition
     *            - the conditions of this {@link IncomingProcessEvent}
     * @param token
     *            - the {@link Token processToken}
     */
    protected AbstractIncomingIntermediateProcessEvent(AdapterConfiguration config,
                                               EventCondition condition,
                                               Token token) {

        super(config, condition, new DefaultTokenResumption(token));
        this.token = token;
        this.node = token.getCurrentNode();
    }

    @Override
    public Token getToken() {

        return token;
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
