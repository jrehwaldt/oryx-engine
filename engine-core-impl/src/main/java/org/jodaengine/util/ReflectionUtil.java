package org.jodaengine.util;

import java.io.InputStream;
import java.net.URL;

import org.jodaengine.exception.JodaEngineRuntimeException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * This utility class offers methods for reflection mechanisms, and things like instantiating a class when given the
 * string name of the class. Be aware that names have to be fully qualified (with the whole package stuff up front)
 */
public final class ReflectionUtil {

    private static Logger logger = LoggerFactory.getLogger(ReflectionUtil.class);

    /**
     * No public constructors for utillity classes.
     */
    private ReflectionUtil() {

    };

    /**
     * Gets the class loader in the context of the current thread.
     * 
     * @return the class loader
     */
    public static ClassLoader getClassLoader() {

        ClassLoader loader = Thread.currentThread().getContextClassLoader();
        return loader;
    }

    /**
     * Tries to load the given class.
     * 
     * @param className
     *            the full qualified name of the class to load, is null if the classloader can't be loaded failed.
     * @return the class
     */
    public static Class<?> loadClass(String className) {

        Class<?> clazz = null;
        ClassLoader classLoader = getClassLoader();
        if (classLoader != null) {
            logger.debug("Trying to load class with custom classloader: " + className);
            clazz = loadClassSilent(className, classLoader);
        }
        return clazz;
    }

    /**
     * Returns an input stream for reading the specified resource.
     * 
     * @param name
     *            the name of the resource.
     * @return the resource as stream
     */
    public static InputStream getResourceAsStream(String name) {

        InputStream resourceStream = null;
        ClassLoader classLoader = getClassLoader();
        if (classLoader != null) {
            resourceStream = classLoader.getResourceAsStream(name);
        }

        if (resourceStream == null) {
            // Finally, try the classloader for this class
            classLoader = ReflectionUtil.class.getClassLoader();
            resourceStream = classLoader.getResourceAsStream(name);
        }
        return resourceStream;
    }

    /**
     * Finds the resource with the given name. A resource is some data (images, audio, text, etc) that can be accessed
     * by class code in a way that is independent of the location of the code.
     * 
     * @param name
     *            the name of the resource
     * @return An URL object for reading the resource, or null if the resource could not be found or the invoker doesn't
     *         have adequate privileges to get the resource.
     */
    public static URL getResource(String name) {

        URL url = null;
        ClassLoader classLoader = getClassLoader();
        if (classLoader != null) {
            url = classLoader.getResource(name);
        }
        if (url == null) {
            // Finally, try the classloader for this class
            classLoader = ReflectionUtil.class.getClassLoader();
            url = classLoader.getResource(name);
        }

        return url;
    }

    /**
     * Instantiates the class with the given classname. If it is not found, a Dalmatine RuntimeException is thrown.
     * 
     * @param className
     *            the name of the class to be instantiated
     * @return the instance of the class
     */
    public static Object instantiate(String className) {

        try {
            Class<?> clazz = loadClass(className);
            return clazz.newInstance();
        } catch (Exception e) {
            String errorMessage = "Class " + className + " could not be instantiated.";
            logger.error(errorMessage, e);
            throw new JodaEngineRuntimeException(errorMessage, e);
        }
    }

    /**
     * Loads a class "silently" so no error if this doesn't succeed, in this case null is simply returned.
     * 
     * @param className
     *            the class name
     * @param classLoader
     *            the class loader
     * @return the class
     */
    private static Class<?> loadClassSilent(String className, ClassLoader classLoader) {

        try {
            return Class.forName(className, true, classLoader);
        } catch (ClassNotFoundException classNotFoundException) {
            logger.error("The class " + className + " could not be loaded.", classNotFoundException);
            return null;
        }
    }
}
