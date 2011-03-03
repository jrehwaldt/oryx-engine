package de.hpi.oryxengine.ext.rest;

import javax.annotation.Nonnull;
import javax.servlet.http.HttpServlet;

/**
 * Container for servlet creation.
 * 
 * @author Jan Rehwaldt
 * @since 2011-03-03
 */
public final class ServletDescriptor {
    
    private final @Nonnull String contextPath;
    private final @Nonnull HttpServlet servlet;
    
    /**
     * Default constructor.
     * 
     * @param servlet the servlet
     * @param contextPath the path, the servlet should be bound to
     */
    public ServletDescriptor(@Nonnull HttpServlet servlet,
                             @Nonnull String contextPath) {
        this.servlet = servlet;
        
        if (!contextPath.startsWith("/")) {
            contextPath = "/" + contextPath;
        }
        
        this.contextPath = contextPath;
    }

    /**
     * Returns the servlet's context path.
     * 
     * @return the servlet's context path
     */
    public String getContextPath() {
        return contextPath;
    }

    /**
     * Returns the servlet.
     * 
     * @return the servlet
     */
    public HttpServlet getServlet() {
        return servlet;
    }
}
