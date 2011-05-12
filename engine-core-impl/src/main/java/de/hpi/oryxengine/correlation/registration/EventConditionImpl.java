package de.hpi.oryxengine.correlation.registration;

import java.lang.reflect.Method;

/**
 * The Class EventConditionImpl. Have a look at the interface {@link EventCondition}.
 */
public class EventConditionImpl implements EventCondition {

    /**
	 * @uml.property  name="method"
	 */
    private Method method;
    /**
	 * @uml.property  name="expectedValue"
	 */
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
	 * @return
	 * @uml.property  name="method"
	 */
    @Override
    public Method getMethod() {

        return method;
    }

    /**
	 * @return
	 * @uml.property  name="expectedValue"
	 */
    @Override
    public Object getExpectedValue() {

        return expectedValue;
    }

}
