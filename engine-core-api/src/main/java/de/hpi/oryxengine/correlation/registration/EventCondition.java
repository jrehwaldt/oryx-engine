package de.hpi.oryxengine.correlation.registration;

import java.lang.reflect.Method;

/**
 * The Interface EventCondition.
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
