package org.jodaengine.deployment;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A ClassLoader for the JodaEngine, that serves classes, that were defined in a deployment package.
 */
public class CustomClassLoader extends ClassLoader implements ClassContainer {

    private Map<String, byte[]> classes;
    private final Logger logger = LoggerFactory.getLogger(getClass());
    
    /**
     * Instantiates a new custom class loader.
     */
    public CustomClassLoader() {
        super();
        classes = new HashMap<String, byte[]>();
    }
    
    @Override
    public Class<?> findClass(String name) throws ClassNotFoundException {
        logger.debug("This method has been called");
        Class<?> classToReturn = findLoadedClass(name);
        logger.debug("Searching for class {}", name);
        if (classToReturn == null) {
            try {
                classToReturn = findSystemClass(name);
            } catch (ClassNotFoundException e) {
                byte[] classAsBytes = classes.get(name);     
                
                if (classAsBytes == null) {
                    logger.error("Class {} could not be found", name);
                    throw new ClassNotFoundException();
                }
                classToReturn = defineClass(name, classAsBytes, 0, classAsBytes.length);
            }
            
        }
        return classToReturn;
    }
    
    @Override
    public void addLoadableClass(String className, byte[] classData) {
        classes.put(className, classData);
    }
}
