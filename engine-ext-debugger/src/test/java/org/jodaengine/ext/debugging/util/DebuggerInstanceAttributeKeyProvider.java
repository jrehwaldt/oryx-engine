package org.jodaengine.ext.debugging.util;

import org.jodaengine.ext.debugging.shared.DebuggerInstanceAttribute;

/**
 * This class provides access to the {@link DebuggerInstanceAttribute}'s attribute key,
 * which is not visible by default.
 * 
 * @author Jan Rehwaldt
 * @since 2011-06-27
 */
public final class DebuggerInstanceAttributeKeyProvider extends DebuggerInstanceAttribute {
    private static final long serialVersionUID = -1823120841562347026L;
    
    /**
     * Provides access to the private attribute key.
     * 
     * @return the attribute key.
     */
    public static String getAttributeKey() {
        return ATTRIBUTE_KEY;
    }
}
