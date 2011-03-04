package de.hpi.oryxengine.exception;

/**
 * Runtime exception that is the superclass of all OryxEngine exceptions.
 * 
 * @author Gerardo Navarro Suarez
 */
public class OryxEngineException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public OryxEngineException(String message, Throwable cause) {

        super(message, cause);
    }

    public OryxEngineException(String message) {

        super(message);
    }
}
