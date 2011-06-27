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

/**
 * A class encapsulating all the method invocation and Reflection stuff.
 */
public abstract class AbstractMethodInvokingEventCondition {

    protected Class<? extends AdapterEvent> clazzOfMethod;
    protected Method method;
    protected final Logger logger = LoggerFactory.getLogger(getClass());

    /**
     * Instantiates a new abstract method invoking event condition.
     *
     * @param clazz
     *            - the class of the object where the method should be called
     * @param methodName
     *            - the name of the method which should be called
     */
    public AbstractMethodInvokingEventCondition(@Nonnull Class<? extends AdapterEvent> clazz,
                                                @Nonnull String methodName) {
        this.clazzOfMethod = clazz;
        // In case the method does not exist an exception is thrown
        this.method = ReflectionUtil.getMethodFor(clazz, methodName);
    }

    /**
     * Encapsulates the invocation of the method.
     * 
     * @param adapterEvent
     *            - the {@link AdapterEvent} where the method is invoked on
     * @return the returnValue of the method invocation
     */
    protected Object invokeMethod(AdapterEvent adapterEvent) {
    
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

    /**
     * Checks if is instance of desired class.
     *
     * @param adapterEvent the adapter event
     * @return true, if is instance of desired class
     */
    protected boolean isInstanceOfDesiredClass(AdapterEvent adapterEvent) {
    
        if (!clazzOfMethod.isInstance(adapterEvent)) {
            String debugMessage = "The class of the adapterEvent '" + adapterEvent.getClass() + "' occured at '"
                + adapterEvent.getTimestamp() + "' does not match to the desired class '"
                + clazzOfMethod.getCanonicalName() + "'.";
            logger.debug(debugMessage);
            return false;
        } else {
            return true;
        }
    }

}
