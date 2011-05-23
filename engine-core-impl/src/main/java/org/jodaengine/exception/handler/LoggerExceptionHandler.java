package org.jodaengine.exception.handler;

import org.jodaengine.process.token.Token;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The LoggerExceptionHandler logs the exception messages.
 */
public class LoggerExceptionHandler extends AbstractExceptionHandler {
    
    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    protected void processExceptionLocally(Exception exception, Token token) {

        logger.error("An Exception occurred: {}", exception.getMessage());
        
    }

}
