package de.hpi.oryxengine.correlation.registration;

import java.lang.reflect.Method;

/**
 * The Class EventConditionImpl.
 */
public class EventConditionImpl implements EventCondition {

    private Method method;
    private Object expectedValue;
    
    /**
     * Instantiates a new event condition impl.
     *
     * @param method the method
     * @param expectedValue the expected value
     */
    public EventConditionImpl(Method method, Object expectedValue) {
        this.method = method;
        this.expectedValue = expectedValue;
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public Method getMethod() {

        return method;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Object getExpectedValue() {

        return expectedValue;
    }

}
