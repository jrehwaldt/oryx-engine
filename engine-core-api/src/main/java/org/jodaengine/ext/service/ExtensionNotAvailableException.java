package org.jodaengine.ext.service;

/**
 * This exception is used to identify a requested extension, which is not available.
 * 
 * @author Jan Rehwaldt
 * @since 2011-05-19
 */
public class ExtensionNotAvailableException extends Exception {
    private static final long serialVersionUID = 5042337668671589864L;
    
    private final static String EXCEPTION_MESSAGE = "The defined extension is not available";
    
    /**
     * Default constructor.
     */
    public ExtensionNotAvailableException() {
        super(EXCEPTION_MESSAGE);
    }
}
