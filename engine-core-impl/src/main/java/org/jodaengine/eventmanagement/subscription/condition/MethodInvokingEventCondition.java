package org.jodaengine.eventmanagement.subscription.condition;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import javax.annotation.Nonnull;

import org.jodaengine.eventmanagement.AdapterEvent;
import org.jodaengine.exception.JodaEngineRuntimeException;
import org.jodaengine.util.ReflectionUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * It is a single condition that holds a method and an expected return value. It and can be
 * tested against a correlated object.
 */
public class MethodInvokingEventCondition implements EventCondition {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    private Class<? extends AdapterEvent> clazzOfMethod;
    private Method method;
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

        // In case the method does not exist an exception is thrown
        this.method = ReflectionUtil.getMethodFor(clazz, methodName);
        this.clazzOfMethod = clazz;
        this.expectedValue = expectedValue;
    }

    @Override
    public boolean evaluate(AdapterEvent adapterEvent) {

        if (!adapterEvent.getClass().equals(clazzOfMethod)) {
            String debugMessage = "The class of the adapterEvent '" + adapterEvent.getClass() + "' occured at '"
                + adapterEvent.getTimestamp() + "' does not match to the desired class '"
                + clazzOfMethod.getCanonicalName() + "'.";
            logger.debug(debugMessage);
            return false;
        }

        Object returnValue = invokeMethod(adapterEvent);

        return returnValue.equals(expectedValue);
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
