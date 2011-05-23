package org.jodaengine.exception.handler;

import org.jodaengine.process.instance.AbstractProcessInstance;
import org.jodaengine.process.token.Token;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The InstanceTerminationHandler terminates the process instance, in which context the exception occurred.
 * This is not done immediately, but after all of the concurrently executing tokens of this instance have finished their
 * current execution.
 */
public class InstanceTerminationHandler extends AbstractExceptionHandler {

    private final Logger logger = LoggerFactory.getLogger(getClass());
    
    @Override
    protected void processExceptionLocally(Exception exception, Token token) {

        AbstractProcessInstance corruptInstance = token.getInstance();
        logger.error("Cancelling execution of process instance {}", corruptInstance.getID());
        token.getNavigator().cancelProcessInstance(corruptInstance);

    }

}
