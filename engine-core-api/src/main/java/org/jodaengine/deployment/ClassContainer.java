package org.jodaengine.deployment;

/**
 * Can be implemented by classes that save byte-Data for later Class-Creation.
 */
public interface ClassContainer {

    void addLoadableClass(String className, byte[] classData);
}
