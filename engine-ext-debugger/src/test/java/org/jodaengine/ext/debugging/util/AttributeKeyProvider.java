package org.jodaengine.ext.debugging.util;

import org.jodaengine.ext.debugging.shared.DebuggerAttribute;

/**
 * This class provides access to the {@link DebuggerAttribute}'s attribute key,
 * which is not visible by default.
 * 
 * @author Jan Rehwaldt
 * @since 2011-05-24
 */
public final class AttributeKeyProvider extends DebuggerAttribute {
    private static final long serialVersionUID = 175105304724615391L;

    /**
     * Provides access to the private attribute key.
     * 
     * @return the attribute key.
     */
    public static String getAttributeKey() {
        return ATTRIBUTE_KEY;
    }
}
