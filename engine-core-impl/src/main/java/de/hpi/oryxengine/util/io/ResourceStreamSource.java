package de.hpi.oryxengine.util.io;

import java.io.InputStream;

import de.hpi.oryxengine.exception.DalmatinaRuntimeException;
import de.hpi.oryxengine.util.ReflectionUtil;

// TODO [@Gerado] Javadoc should be added
public class ResourceStreamSource implements StreamSource {

    String resource;
    ClassLoader classLoader;

    public ResourceStreamSource(String resource) {

        this.resource = resource;
    }

    public ResourceStreamSource(String resource, ClassLoader classLoader) {

        this.resource = resource;
        this.classLoader = classLoader;
    }

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

    public String toString() {

        return "Resource[" + resource + "]";
    }

    @Override
    public String getType() {

        // TODO Auto-generated method stub
        return null;
    }
}
