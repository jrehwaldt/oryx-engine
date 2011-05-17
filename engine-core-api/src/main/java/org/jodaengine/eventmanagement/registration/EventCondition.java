package org.jodaengine.eventmanagement.registration;

import java.lang.reflect.Method;

/**
 * The Interface EventCondition. It is a single condition that holds a method and an expected return value and can be
 * tested against a correlated object.
 */
public interface EventCondition {

    /**
     * Gets the method.
     * 
     * @return the method
     */
    Method getMethod();

    /**
     * Gets the expected value.
     * 
     * @return the expected value
     */
    Object getExpectedValue();
}
