package org.jodaengine.eventmanagement.processevent.incoming.condition.simple;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import javax.annotation.Nonnull;

import org.jodaengine.eventmanagement.AdapterEvent;
import org.jodaengine.eventmanagement.subscription.condition.EventCondition;
import org.jodaengine.exception.JodaEngineRuntimeException;
import org.jodaengine.util.ReflectionUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

// TODO @TobiP: Create a beautiful superclass encapsulating the whole lot of stuff this one and MethodInvokingEventCondition have in common

/**
 * Checks that the return value of a method returns a specified String.
 */
public class MethodReturnsStringContainingEventCondition implements EventCondition {

    private Class<? extends AdapterEvent> clazzOfMethod;
    private Method method;
    private String expectedSubString;
    
    private final Logger logger = LoggerFactory.getLogger(getClass());
    
    public MethodReturnsStringContainingEventCondition(@Nonnull Class<? extends AdapterEvent> clazz,
                                                       @Nonnull String methodName,
                                                       @Nonnull String expectedValue) {

       // In case the method does not exist an exception is thrown
       this.method = ReflectionUtil.getMethodFor(clazz, methodName);
       this.clazzOfMethod = clazz;
       this.expectedSubString = expectedValue;
    }
    
    @Override
    public boolean evaluate(AdapterEvent adapterEvent) {

        if (!clazzOfMethod.isInstance(adapterEvent)) {
            String debugMessage = "The class of the adapterEvent '" + adapterEvent.getClass() + "' occured at '"
                + adapterEvent.getTimestamp() + "' does not match to the desired class '"
                + clazzOfMethod.getCanonicalName() + "'.";
            logger.debug(debugMessage);
            return false;
        }

        String returnValue = (String) invokeMethod(adapterEvent);

        return returnValue.contains(expectedSubString);
    }
    
    /**
     * Encapsulates the invocation of the method.
     * 
     * @param adapterEvent
     *            - the {@link AdapterEvent} where the method is invoked on
     * @return the returnValue of the method invocation
     */
    private Object invokeMethod(AdapterEvent adapterEvent) {

        Object returnValue = null;
        try {

            returnValue = method.invoke(adapterEvent);

        } catch (IllegalArgumentException e) {
            logAndThrowException(e);

        } catch (IllegalAccessException e) {
            logAndThrowException(e);

        } catch (InvocationTargetException e) {
            logAndThrowException(e);
        }

        return returnValue;
    }

    /**
     * Log and throws the exception as a {@link JodaEngineRuntimeException}.
     * 
     * @param exception
     *            - the exception that should be logged and thrown
     * @throws JodaEngineRuntimeException
     */
    private void logAndThrowException(Exception exception) {

        String errorMessage = "An error occured while invoking the method '" + method.getName() + "' of the class '"
            + clazzOfMethod.getCanonicalName() + "'. Here is the message: " + exception.getMessage();
        logger.error(errorMessage, exception);
        throw new JodaEngineRuntimeException(errorMessage, exception);
    }

}
