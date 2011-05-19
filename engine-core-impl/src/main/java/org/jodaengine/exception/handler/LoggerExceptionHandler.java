package org.jodaengine.exception.handler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.jodaengine.exception.JodaEngineRuntimeException;
import org.jodaengine.process.token.Token;

/**
 * The LoggerExceptionHandler logs the exception messages.
 */
public class LoggerExceptionHandler extends AbstractJodaRuntimeExceptionHandler {
    
    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    protected void processExceptionLocally(JodaEngineRuntimeException exception, Token token) {

        logger.error("An Exception occurred: {}", exception.getMessage());
        
    }

}
