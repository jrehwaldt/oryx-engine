package org.jodaengine.util;

/**
 * A generic interface that is designed to provide the possibility to append certain pattern on another pattern.
 * 
 * @param <T>
 *            - the type of the classes that are chained together
 */
public interface PatternAppendable<T> {

    /**
     * Defines which is the continuing pattern.
     * 
     * @param nextPattern
     *            - the continuing pattern
     * @return the next pattern of this object
     */
    T setNextPattern(T nextPattern);
}
