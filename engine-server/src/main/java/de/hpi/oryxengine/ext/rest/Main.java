package de.hpi.oryxengine.ext.rest;

import org.eclipse.jetty.server.Server;

/**
 * This is the main class for a server providing 
 * a REST-interface.
 * 
 * @author Jan Rehwaldt
 * @since 2011-03-03
 */
public final class Main {
    
    /**
     * Hidden constructor.
     */
    private Main() {
        
    }
    
    /**
     * Starting point for our server.
     * 
     * Open http://localhost:8380/rest/ and follow a link.
     * 
     * @param args the main constructor arguments
     * @throws Exception thrown if the server was unable to start
     */
    public static void main(String... args) throws Exception {
        final Server server = AppContextBuilder.buildServer();
        
        server.start();
        server.join();
    }
    
}
