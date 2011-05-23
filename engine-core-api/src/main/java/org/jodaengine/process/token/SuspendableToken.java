package org.jodaengine.process.token;

/**
 * 
 * @author Gery
 *
 */
public interface SuspendableToken extends Token {

    /**
     * Stopping the token navigation.
     */
    void suspend();

    /**
     * Continuing the token navigation.
     * 
     */
    // TODO Info-Object muss übergeben werden von wem das Token resumed wurde
    void resume();
}
