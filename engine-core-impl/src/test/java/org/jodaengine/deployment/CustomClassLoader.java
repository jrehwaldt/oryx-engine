package org.jodaengine.deployment;

import java.util.HashMap;
import java.util.Map;

/**
 * A ClassLoader for the JodaEngine, that serves classes, that were defined in a deployment package.
 */
public class CustomClassLoader extends ClassLoader implements ClassContainer {

    private Map<String, byte[]> classes;
    
    public CustomClassLoader() {
        super();
        classes = new HashMap<String, byte[]>();
    }
    
    @Override
    public Class<?> findClass(String name) throws ClassNotFoundException {
        Class<?> classToReturn = findLoadedClass(name);
        if (classToReturn == null) {
            byte[] classAsBytes = classes.get(name);     
            
            if (classAsBytes == null) {
                throw new ClassNotFoundException();
            }
            classToReturn = defineClass(name, classAsBytes, 0, classAsBytes.length);
        }
        return classToReturn;
    }
    
    @Override
    public void addLoadableClass(String className, byte[] classData) {
        classes.put(className, classData);
    }
}
