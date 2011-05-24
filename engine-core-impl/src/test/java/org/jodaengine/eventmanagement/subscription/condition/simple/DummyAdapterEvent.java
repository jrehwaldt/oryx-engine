package org.jodaengine.eventmanagement.subscription.condition.simple;

import org.jodaengine.eventmanagement.AdapterEvent;
import org.jodaengine.eventmanagement.adapter.AbstractAdapterEvent;

/**
 * Only a dummy {@link AdapterEvent} for the {@link MethodInvokingEventConditionTest}.
 */
public class DummyAdapterEvent extends AbstractAdapterEvent {

    public final static String METHOD_RETURNS_STRING = "dummyMethodReturnsString";
    public final static String METHOD_RETURNS_INT = "dummyMethodReturnsInt";

    private String stringToReturn;
    private int intToReturn;
    
    /**
     * Constructor that has to exist.
     */
    public DummyAdapterEvent(String stringToReturn, int intToReturn) {

        super(null);
        
        this.stringToReturn = stringToReturn;
        this.intToReturn = intToReturn;
    }

    /**
     * Dummy method that should be called by the {@link MethodInvokingEventCondition}.
     */
    public String dummyMethodReturnsString() {

        return stringToReturn;
    }

    /**
     * Dummy method that should be called by the {@link MethodInvokingEventCondition}.
     */
    public int dummyMethodReturnsInt() {

        return intToReturn;
    }
}
