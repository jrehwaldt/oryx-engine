package org.jodaengine.ext.debugging.rest;

import java.util.UUID;

import javax.annotation.Nonnull;

import org.jodaengine.exception.JodaEngineRuntimeException;

/**
 * This exception is thrown if a reference could not be resolved.
 * 
 * @author Jan Rehwaldt
 * @since 2011-05-31
 */
public class DereferencedObjectException extends JodaEngineRuntimeException {
    private static final long serialVersionUID = -7910631163798334665L;
    
    private final Class<?> targetClass;
    private final UUID targetID;
    
    /**
     * Default constructor.
     * 
     * @param targetClass the target type
     * @param targetID the target id, which could not be found
     */
    public DereferencedObjectException(@Nonnull Class<?> targetClass,
                                       @Nonnull UUID targetID) {
        this.targetClass = targetClass;
        this.targetID = targetID;
    }

    /**
     * Returns the target class.
     * 
     * @return the target class
     */
    public Class<?> getTargetClass() {
        return this.targetClass;
    }

    /**
     * Returns the target id.
     * 
     * @return the target id
     */
    public UUID getTargetID() {
        return this.targetID;
    }
    
    @Override
    public String toString() {
        return String.format("Reference[%s, type=%s]", this.targetID, this.targetClass.getSimpleName());
    }
}
