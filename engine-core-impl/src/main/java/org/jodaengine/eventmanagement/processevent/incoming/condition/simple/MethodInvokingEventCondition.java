package org.jodaengine.eventmanagement.processevent.incoming.condition.simple;


import javax.annotation.Nonnull;

import org.jodaengine.eventmanagement.AdapterEvent;
import org.jodaengine.eventmanagement.subscription.condition.EventCondition;

/**
 * It is a single condition that holds a method and an expected return value. It and can be
 * tested against a correlated object.
 */
public class MethodInvokingEventCondition extends AbstractMethodInvokingEventCondition implements EventCondition {

    private Object expectedValue;

    /**
     * Default instantiation for the InvokingMethodEventCondition.
     * 
     * @param clazz
     *            - the class of the object where the method should be called
     * @param methodName
     *            - the name of the method which should be called
     * @param expectedValue
     *            - the value that is expected as result of the invoked method
     */
    public MethodInvokingEventCondition(@Nonnull Class<? extends AdapterEvent> clazz,
                                        @Nonnull String methodName,
                                        @Nonnull Object expectedValue) {

        
        super(clazz, methodName);
        this.expectedValue = expectedValue;
    }

    @Override
    public boolean evaluate(AdapterEvent adapterEvent) {

         if (!isInstanceOfDesiredClass(adapterEvent)) {
             return false;
        }

        Object returnValue = invokeMethod(adapterEvent);

        return returnValue.equals(expectedValue);
    }
}
