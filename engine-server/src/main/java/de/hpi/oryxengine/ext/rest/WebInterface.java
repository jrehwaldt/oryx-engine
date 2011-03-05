package de.hpi.oryxengine.ext.rest;

import javax.annotation.Nonnull;

import org.eclipse.jetty.server.Server;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.hpi.oryxengine.navigator.Navigator;

/**
 * A web interface server for a certain navigator instance.
 * 
 * @author Jan Rehwaldt
 * @since 2011-03-05
 */
public final class WebInterface {
    
    private final Logger logger = LoggerFactory.getLogger(getClass());
    
    private final Server server;
    private final Navigator navigator;
    
    /**
     * Sets up a server instance for a certain navigator instance.
     * Server needs to be started explicitly.
     * 
     * @param navigator the navigator, which should be accessible via web
     * @throws Exception thrown if building the server fails (e.g. no config found)
     */
    public WebInterface(@Nonnull Navigator navigator)
    throws Exception {
        this.navigator = navigator;
        
        final AppContextBuilder builder = new AppContextBuilder(this.navigator);
        this.server = builder.buildServer();
    }
    
    /**
     * Starts the server non-blocked.
     * 
     * @throws Exception thrown if starting fails
     */
    public void start()
    throws Exception {
        this.server.start();
    }
    
    /**
     * Starts the web interface and blocks until it is stopped.
     * 
     * @throws Exception if starting fails
     */
    public void startBlocking()
    throws Exception {
        start();
        try {
            this.server.join();
        } catch (InterruptedException ie) {
            logger.info("Server stopped.");
        }
    }
    
    /**
     * Stops the web interface.
     * 
     * @throws Exception thrown if stopping fails
     */
    public void stop()
    throws Exception {
        this.server.stop();
    }
}
