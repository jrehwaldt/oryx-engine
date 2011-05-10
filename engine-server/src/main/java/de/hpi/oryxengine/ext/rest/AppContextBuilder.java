package de.hpi.oryxengine.ext.rest;

import javax.annotation.Nonnull;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.util.resource.Resource;
import org.eclipse.jetty.xml.XmlConfiguration;

import de.hpi.oryxengine.navigator.Navigator;

/**
 * This class builds a web context for the rest-part of the joda engine.
 * 
 * @author Jan Rehwaldt
 * @since 2011-03-03
 */
public final class AppContextBuilder {
    
    private static final String SERVER_CONFIG_FILE = "server.xml";
    
    private final Navigator navigator;
    private final String configFile;
    
    /**
     * Constructor with setting-file adjustments.
     * 
     * @param navigator the navigator, which should be controlled
     * @param configFile the server's config file (generally server.xml)
     */
    protected AppContextBuilder(@Nonnull Navigator navigator,
                                @Nonnull String configFile) {
        this.navigator = navigator;
        this.configFile = configFile;
    }
    
    /**
     * Default constructor searching for a configuration file server.xml.
     * 
     * @param navigator the navigator, which should be controlled
     */
    protected AppContextBuilder(@Nonnull Navigator navigator) {
        this(navigator, SERVER_CONFIG_FILE);
    }
    
    /**
     * Builds a fully configured server.
     * 
     * @return the builded server configured via server.xml in the server's root folder
     * @throws Exception thrown if the config file is not loadable or the server configuration failed
     */
    public Server buildServer()
    throws Exception {
        if (this.navigator == null) {
            throw new IllegalArgumentException();
        }
        
        final Resource resource = Resource.newSystemResource(this.configFile);
        final XmlConfiguration config = new XmlConfiguration(resource.getInputStream());
        return (Server) config.configure();
    }
}
