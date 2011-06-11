package org.jodaengine.node.activity;

import javax.annotation.Nonnull;

import org.jodaengine.exception.JodaEngineRuntimeException;
import org.jodaengine.process.token.AbstractToken;
import org.jodaengine.process.token.Token;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The Class AbstractActivityImpl.
 * An activity is the behaviour of a node. It does not perform any control flow routing operations.
 */
public abstract class AbstractActivity implements Activity {

    protected final Logger logger = LoggerFactory.getLogger(getClass());

    /**
     * Instantiates a new activity. State is set to INIT, but observers will not
     * be notified (since no observer are registered so far).
     * 
     */
    protected AbstractActivity() {

    }

    @Override
    public void execute(@Nonnull Token token) {

        // The token is transformed into an AbstractToken so that the execute method of the activity is provided with
        // more methods
        executeIntern((AbstractToken) token);
    }

    /**
     * Method, which implements the concrete's activity's implementation.
     * 
     * @param token
     *            the instance this activity operates on
     */
    protected abstract void executeIntern(@Nonnull AbstractToken token);

    /**
     * Override this, if need to cleanup, if the activity is cancelled (e.g. event unsubscription).
     * 
     * @param executingToken
     *            the executing token
     */
    @Override
    public void cancel(Token executingToken) {

    }

    /**
     * Override this, if you need to do cleanup after the executing token has been resumed.
     * 
     * @param token
     *            - the token that resumes this activity
     * @param resumeObject
     *            - an object that is passed from class that resumes the Token
     */
    @Override
    public void resume(Token token, Object resumeObject) {

    }

    @Override
    public String toString() {

        return getClass().getSimpleName();
    }

    /**
     * It is a utility method that can be used to throw an exception which is also logged.
     * 
     * @param errorMessage
     *            - that message that describes the exception
     */
    protected void logAndThrowError(String errorMessage) {

        logger.error(errorMessage);
        throw new JodaEngineRuntimeException(errorMessage);
    }
}
