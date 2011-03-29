package de.hpi.oryxengine.worklist.gui.util;

import javax.annotation.Nonnull;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.util.resource.Resource;
import org.eclipse.jetty.xml.XmlConfiguration;

/**
 * This class builds a web context for the rest-part of the oryx engine.
 * 
 * @author Jan Rehwaldt
 * @since 2011-03-24
 */
public final class AppContextBuilder {
    
    private static final String SERVER_CONFIG_FILE = "server.xml";
    
    private final String configFile;
    
    /**
     * Constructor with setting-file adjustments.
     * 
     * @param configFile the server's config file (generally server.xml)
     */
    protected AppContextBuilder(@Nonnull String configFile) {
        this.configFile = configFile;
    }
    
    /**
     * Default constructor searching for a configuration file server.xml.
     */
    protected AppContextBuilder() {
        this(SERVER_CONFIG_FILE);
    }
    
    /**
     * Builds a fully configured server.
     * 
     * @return the builded server configured via server.xml in the server's root folder
     * @throws Exception thrown if the config file is not loadable or the server configuration failed
     */
    public Server buildServer()
    throws Exception {
        final Resource resource = Resource.newSystemResource(this.configFile);
        final XmlConfiguration config = new XmlConfiguration(resource.getInputStream());
        
        final Server server = (Server) config.configure();
        return server;
    }
}
