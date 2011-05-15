package org.jodaengine.util.io;

import org.jodaengine.exception.JodaEngineRuntimeException;
import org.jodaengine.util.ReflectionUtil;

import java.io.InputStream;


/**
 * A source for resources. Provides access to process resources.
 */
public class ClassPathResourceStreamSource implements StreamSource {

    private String resource;
    private ClassLoader classLoader;
    
    /**
     * Default constructor.
     * 
     * @param resource the resource to handle
     */
    public ClassPathResourceStreamSource(String resource) {

        this.resource = resource;
    }
    
    /**
     * Constructor with classpath extension. Allows to define an class loader.
     * 
     * @param resource the resource to handle
     * @param classLoader the class loader
     */
    public ClassPathResourceStreamSource(String resource, ClassLoader classLoader) {

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
            throw new JodaEngineRuntimeException(errorMessage);
        }

        return inputStream;
    }

    @Override
    public String toString() {

        return getName();
    }

    @Override
    public String getName() {

        return "Resource[" + resource + "]";
    }
}
