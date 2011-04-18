package de.hpi.oryxengine.util.io;

import java.io.InputStream;

import de.hpi.oryxengine.exception.DalmatinaRuntimeException;
import de.hpi.oryxengine.util.ReflectionUtil;

/**
 * A source for resources. Provides access to process resources.
 */
public class ResourceStreamSource implements StreamSource {

    private String resource;
    private ClassLoader classLoader;
    
    /**
     * Default constructor.
     * 
     * @param resource the resource to handle
     */
    public ResourceStreamSource(String resource) {

        this.resource = resource;
    }
    
    /**
     * Constructor with classpath extension. Allows to define an class loader.
     * 
     * @param resource the resource to handle
     * @param classLoader the class loader
     */
    public ResourceStreamSource(String resource, ClassLoader classLoader) {

        this.resource = resource;
        this.classLoader = classLoader;
    }

    @Override
    public InputStream getInputStream() {

        InputStream inputStream = null;
        if (classLoader == null) {
            inputStream = ReflectionUtil.getResourceAsStream(resource);
        } else {
            inputStream = classLoader.getResourceAsStream(resource);
        }

        if (inputStream == null) {
            String errorMessage = "The resource '" + resource + "' does not exist.";
            throw new DalmatinaRuntimeException(errorMessage);
        }

        return inputStream;
    }

    @Override
    public String toString() {

        return "Resource[" + resource + "]";
    }

    @Override
    public String getType() {

        // TODO Auto-generated method stub
        return null;
    }
}
