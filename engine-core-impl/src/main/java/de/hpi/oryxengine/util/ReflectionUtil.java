package de.hpi.oryxengine.util;

import java.io.InputStream;
import java.net.URL;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.hpi.oryxengine.OryxEngineAppContext;
import de.hpi.oryxengine.exception.DalmatinaRuntimeException;

/**
 * This utility class offers methods for reflection mechanisms.
 */
public final class ReflectionUtil {

    private static final Logger logger = LoggerFactory.getLogger(ReflectionUtil.class);

    public static ClassLoader getClassLoader() {

        ClassLoader loader = Thread.currentThread().getContextClassLoader();
        return loader;
    }

    public static Class<?> loadClass(String className) {
        Class<?> clazz = null;
        ClassLoader classLoader = getClassLoader();
        if (classLoader != null) {
          logger.debug("Trying to load class with custom classloader: " + className);
          clazz = loadClassSilent(className, classLoader);
        }
        return clazz;
    }

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

    public static Object instantiate(String className) {

        try {
            Class<?> clazz = loadClass(className);
            return clazz.newInstance();
        } catch (Exception e) {
            String errorMessage = "Class " + className + " could not be instantiated.";
            logger.error(errorMessage,e);
            throw new DalmatinaRuntimeException(errorMessage, e);
        }
    }

    private static Class<?> loadClassSilent(String className, ClassLoader classLoader) {

        try {
            return Class.forName(className, true, classLoader);
        } catch (ClassNotFoundException classNotFoundException) {
            logger.error("The class " + className + " could not be loaded.", classNotFoundException);
            return null;
        }
    }
}
