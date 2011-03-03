package de.hpi.oryxengine.ext.rest;

import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.ContextHandlerCollection;

/**
 * This is the main class for a server providing 
 * a REST-interface.
 * 
 * @author Jan Rehwaldt
 * @since 2011-03-03
 */
public final class Main {
    
    private static final String SERVER_ROOT = "server/";
    
    /**
     * Hidden constructor.
     */
    private Main() {
        
    }
    
    /**
     * Starting point for our server.
     * 
     * @param args the main constructor arguments
     * @throws Exception thrown if the server was unable to start
     */
    public static void main(String... args) throws Exception {
        
        final AppContextBuilder builder = new AppContextBuilder(SERVER_ROOT);
        
        final Server server = builder.buildServer();
        final ContextHandlerCollection contexts = new ContextHandlerCollection();
        contexts.setHandlers(new Handler[] {
            builder.buildRestWebAppContext(),
            builder.buildResourceWebAppContext()
        });
        server.setHandler(contexts);
        
        server.start();
        server.join();
    }
    
}
