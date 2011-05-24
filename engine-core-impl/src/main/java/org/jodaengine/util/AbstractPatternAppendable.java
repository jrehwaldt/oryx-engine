package org.jodaengine.util;

/**
 * It is the basis for a pattern that is appendable to another pattern.
 * 
 * @param <T>
 *            an arbitrary generic type
 */
public abstract class AbstractPatternAppendable<T> implements PatternAppendable<T> {

    private T nextPattern;

    @Override
    public T setNextPattern(T nextPattern) {

        this.nextPattern = nextPattern;
        return this.nextPattern;
    }

    protected T getNextPattern() {

        return this.nextPattern;
    }
}
