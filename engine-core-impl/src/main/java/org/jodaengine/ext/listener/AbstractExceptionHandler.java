package org.jodaengine.ext.listener;

import org.jodaengine.exception.JodaEngineException;
import org.jodaengine.exception.JodaEngineRuntimeException;
import org.jodaengine.process.token.Token;

/**
 * The Class AbstractExceptionHandler realizes the Chain of Responsibility pattern to handle
 * JodaEngineRuntimeExceptions that occur during the process execution.
 * This allows you to react to these exceptions flexibly.
 * 
 * See {@link org.jodaengine.process.token.TokenImpl} for the use of these classes.
 */
public abstract class AbstractExceptionHandler {

    private AbstractExceptionHandler nextHandler = null;
    
    /**
     * Sets the handler, which follows to this one in the chain of responsibility.
     *
     * @param nextHandler the next handler
     * @return the abstract joda runtime exception handler
     */
    public AbstractExceptionHandler setNext(AbstractExceptionHandler nextHandler) {
        
        this.nextHandler = nextHandler;
        return nextHandler;
    }
    
    /**
     * Process exception. Does local exception handling and forwards it to the next handler.
     *
     * @param exception the exception
     * @param token the token during which execution the exception occurred.
     */
    public void processException(JodaEngineException exception, Token token) {
        processExceptionLocally(exception, token);
        if (nextHandler != null) {
            nextHandler.processException(exception, token);
        }
    }
    
    /**
     * Process exception. Does local exception handling and forwards it to the next handler.
     *
     * @param exception the exception
     * @param token the token during which execution the exception occurred.
     */
    public void processException(JodaEngineRuntimeException exception, Token token) {
        processExceptionLocally(exception, token);
        if (nextHandler != null) {
            nextHandler.processException(exception, token);
        }
    }
    
    /**
     * Process exception locally. To implement in subclasses.
     *
     * @param exception the exception
     * @param token the token during which execution the exception occurred.
     */
    protected abstract void processExceptionLocally(Exception exception, Token token);
}
