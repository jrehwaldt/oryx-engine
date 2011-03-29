package de.hpi.oryxengine.worklist.gui;

import javax.annotation.Nonnull;

import de.hpi.oryxengine.worklist.gui.util.WebInterface;

/**
 * This is the main class for a server providing 
 * a REST-interface.
 * 
 * @author Jan Rehwaldt
 * @since 2011-03-24
 */
public final class WorklistWebServer {
    
    /**
     * Hidden constructor.
     */
    private WorklistWebServer() {
        
    }
    
    /**
     * Starting point for our server.
     * 
     * Open http://localhost:8380/rest/ and follow a link.
     * 
     * @param args the main constructor arguments
     * @throws Exception thrown if the server was unable to start
     */
    public static void main(@Nonnull String... args)
    throws Exception {
        final WebInterface webInterface = new WebInterface();
        webInterface.startBlocking();
    }
    
}
