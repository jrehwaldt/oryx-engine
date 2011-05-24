package org.jodaengine.ext.logger;

import javax.annotation.Nonnull;

import org.jodaengine.ext.Extension;
import org.jodaengine.ext.listener.AbstractTokenListener;
import org.jodaengine.ext.listener.token.ActivityLifecycleChangeEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class is a {@link Logger} and may be injected to observe
 * the lifecycle of certain {@link org.jodaengine.node.activity.AbstractActivity}s.
 */
@Extension("logger-token-listener")
public final class TokenListenerLogger
extends AbstractTokenListener {
    
    private static TokenListenerLogger instance;
    
    private final Logger logger = LoggerFactory.getLogger(getClass());
    
    /**
     * Hide singleton constructor.
     */
    private TokenListenerLogger() { }
    
    /**
     * Returns a lazily initialized logger instance.
     * 
     * @return a logger instance
     */
    public synchronized static @Nonnull TokenListenerLogger getInstance() {
        if (instance == null) {
            instance = new TokenListenerLogger();
        }
        
        return instance;
    }
    
    @Override
    public void stateChanged(@Nonnull ActivityLifecycleChangeEvent event) {
        logger.info("ActivityLifecycle: {}", event);
    }
}
