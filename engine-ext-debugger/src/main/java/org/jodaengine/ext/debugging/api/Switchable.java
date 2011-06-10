package org.jodaengine.ext.debugging.api;

import org.codehaus.jackson.annotate.JsonProperty;

/**
 * Allow a certain class to be enabled or disabled.
 * 
 * @author Jan Rehwaldt
 * @since 2011-05-27
 */
public interface Switchable {

    /**
     * Disable debugging for this debugging element.
     */
    void disable();
    
    /**
     * Enable debugging for this debugging element.
     */
    void enable();
    
    /**
     * Returns true if this debugging element is enabled.
     * 
     * @return true, if debugging is enabled
     */
    @JsonProperty
    boolean isEnabled();
}
