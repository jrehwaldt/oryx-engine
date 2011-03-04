package de.hpi.oryxengine.ext.rest;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.util.resource.Resource;
import org.eclipse.jetty.xml.XmlConfiguration;

/**
 * This class builds a web context for the rest-part of the oryx engine.
 * 
 * @author Jan Rehwaldt
 * @since 2011-03-03
 */
public final class AppContextBuilder {
    
    private static final String SERVER_CONFIG_FILE = "server.xml";
    
    /**
     * Default constructor.
     */
    private AppContextBuilder() {
        
    }
    
    /**
     * Builds a fully configured server. Searches for configuration file as defined
     * in constant SERVER_CONFIG_FILE.
     * 
     * @return the builded server configured via server.xml in the server's root folder
     * @throws Exception thrown if the config file is not loadable or the server configuration failed
     */
    public static Server buildServer()
    throws Exception {
        final Resource configFile = Resource.newSystemResource(SERVER_CONFIG_FILE);
        final XmlConfiguration config = new XmlConfiguration(configFile.getInputStream());
        return (Server) config.configure();
    }
}
