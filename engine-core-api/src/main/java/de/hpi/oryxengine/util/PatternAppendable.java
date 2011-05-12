package de.hpi.oryxengine.util;


public interface PatternAppendable<T> {

    /**
     * 
     * @param nextPattern
     * @return the next pattern
     */
    T setNextPattern(T nextPattern);
}
