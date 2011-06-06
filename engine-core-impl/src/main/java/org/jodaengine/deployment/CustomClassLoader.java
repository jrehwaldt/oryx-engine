package org.jodaengine.deployment;

import java.lang.reflect.Field;
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
     *
     * @param parent the parent class loader
     */
    public CustomClassLoader(ClassLoader parent) {

        super(parent);
        classes = new HashMap<String, byte[]>();
    }

    @Override
    public Class<?> findClass(String name) throws ClassNotFoundException {
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
                try{
                    classToReturn = defineClass(name, classAsBytes, 0, classAsBytes.length);
                } catch (NoClassDefFoundError error) {
                    
                    logClassArray(getAllLoadedClasses(this));
                    logger.debug("###");
                    logClassArray(getAllLoadedClasses(this.getParent()));
                    logger.debug("###");
                    logClassArray(getAllLoadedClasses(getSystemClassLoader()));
                    logger.debug("###");
                    logClassArray(getAllLoadedClasses(Thread.currentThread().getContextClassLoader()));
                }
                
            }
            
        }
        return classToReturn;
    }
    
    private void logClassArray(Class[] classes) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < classes.length; i++) {
            sb.append(classes[i].getName());
            sb.append(" ");
        }
        logger.debug(sb.toString());
    }

    @Override
    public void addLoadableClass(String className, byte[] classData) {

        classes.put(className, classData);
    }

    public static Class[] getAllLoadedClasses(final ClassLoader loader)

    {

        if (loader == null)
            return null;

        // System.out.println ("looking into: " + loader + ", instanceof URLClassLoader: " + (loader instanceof
        // java.net.URLClassLoader));

        try

        {

            Field classes = ClassLoader.class.getDeclaredField("classes");

            classes.setAccessible(true);

            java.util.Vector classesVector = (java.util.Vector) classes.get(loader);

            Class[] result = new Class[classesVector.size()];

            classesVector.copyInto(result);

            return result;

        }

        catch (Exception e)

        {

            // not a canonical JDK implementation: can't do this

            e.printStackTrace();

            return null;

        }

    }

}
