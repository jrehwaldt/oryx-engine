package de.hpi.oryxengine.ext.rest;

import javax.annotation.Nonnull;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.eclipse.jetty.util.resource.Resource;
import org.eclipse.jetty.webapp.WebAppContext;
import org.eclipse.jetty.xml.XmlConfiguration;

/**
 * This class builds a web context for the rest-part of the oryx engine.
 * 
 * @author Jan Rehwaldt
 * @since 2011-03-03
 */
public final class AppContextBuilder {
    
    private static final String RESOURCE_CONTEXT_PATH = "/web";
    private static final String REST_CONTEXT_PATH = "/rest";
    
    private static final String RESOURCE_BASE = "/web";
    private static final String DESCRIPTOR_FILE = "/WEB-INF/web.xml";
    
    private static final String SERVER_CONFIG_FILE = "server.xml";
    
    private String relativeSrverRoot;
    
    /**
     * Default constructor.
     * 
     * @param relativeSrverRoot the relative server root within the context's base folder
     */
    public AppContextBuilder(@Nonnull String relativeSrverRoot) {
        this.relativeSrverRoot = relativeSrverRoot;
    }
    
    /**
     * Builds a rest context and sets it up, appropriately.
     * This context will provide restful servlets and resist under
     * context path as defined by constant REST_CONTEXT_PATH.
     * 
     * @param servlets any servlet, which should be served within this context
     * @return a new web context for our rest server
     */
    public ServletContextHandler buildRestWebAppContext(@Nonnull ServletDescriptor... servlets) {
        final ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
        context.setContextPath(REST_CONTEXT_PATH);
        
        for (ServletDescriptor servlet: servlets) {
            context.addServlet(new ServletHolder(servlet.getServlet()), servlet.getContextPath());
        }
        
        return context;
    }
    
    /**
     * Builds a fully configured server. Searches for configuration file as defined
     * in constant SERVER_CONFIG_FILE.
     * 
     * @return the builded server configured via server.xml in the server's root folder
     * @throws Exception thrown if the config file is not loadable or the server configuration failed
     */
    public Server buildServer()
    throws Exception {
        final Resource configFile = Resource.newSystemResource(SERVER_CONFIG_FILE);
        final XmlConfiguration config = new XmlConfiguration(configFile.getInputStream());
        return (Server) config.configure();
    }
    
    /**
     * Builds a web context and sets it up, appropriately.
     * This context will provide web resources (html, css, jsp, ...) 
     * and resist under context path as defined by constant RESOURCE_CONTEXT_PATH.
     * 
     * @return a new web context for our rest server
     */
    public WebAppContext buildResourceWebAppContext() {
        final WebAppContext context = new WebAppContext();
        context.setDescriptor(context + getAbsoluteServerRoot() + DESCRIPTOR_FILE);
        context.setResourceBase(getAbsoluteServerRoot() + RESOURCE_BASE);
        context.setContextPath(RESOURCE_CONTEXT_PATH);
        
//        context.addServlet(new ServletHolder(new LessServlet()), "*.css");
        
        return context;
    }
    
    /**
     * Returns the absolute server root.
     * 
     * @return an absolute path to the server resources
     */
    private @Nonnull String getAbsoluteServerRoot() {
        final String basePath = System.getProperty("jetty.home", "src/main/resources/");
        return basePath + this.relativeSrverRoot;
    }
}
