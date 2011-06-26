package org.jodaengine.eventmanagement.processevent.incoming.condition.simple;


import javax.annotation.Nonnull;

import org.jodaengine.eventmanagement.AdapterEvent;
import org.jodaengine.eventmanagement.subscription.condition.EventCondition;

// TODO @TobiP: Create a beautiful superclass encapsulating the whole lot of stuff this one and MethodInvokingEventCondition have in common

/**
 * Checks that the return value of a method returns a specified String.
 */
public class MethodReturnsStringContainingEventCondition 
    extends AbstractMethodInvokingEventCondition 
    implements EventCondition {

    private String expectedSubString;
    
    /**
     * Instantiates a new method returns string containing event condition.
     *
     * @param clazz the clazz
     * @param methodName the method name
     * @param expectedValue the expected value
     */
    public MethodReturnsStringContainingEventCondition(@Nonnull Class<? extends AdapterEvent> clazz,
                                                       @Nonnull String methodName,
                                                       @Nonnull String expectedValue) {

       super(clazz, methodName);
       this.expectedSubString = expectedValue;
    }
    
    @Override
    public boolean evaluate(AdapterEvent adapterEvent) {

        if (!isInstanceOfDesiredClass(adapterEvent)) {
            return false;
        }

        String returnValue = (String) invokeMethod(adapterEvent);
        return returnValue.contains(expectedSubString);
    }

}
