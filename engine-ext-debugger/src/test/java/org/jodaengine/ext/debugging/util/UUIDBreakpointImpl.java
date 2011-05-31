package org.jodaengine.ext.debugging.util;

import java.lang.reflect.Field;
import java.util.UUID;

import javax.annotation.Nullable;

import org.jodaengine.ext.debugging.shared.BreakpointImpl;
import org.jodaengine.process.structure.Node;

/**
 * This is a testing implementation, which allows to set the {@link UUID} of a {@link BreakpointImpl} explicitly.
 * 
 * @author Jan Rehwaldt
 * @since 2011-05-31
 */
public class UUIDBreakpointImpl extends BreakpointImpl {
    
    /**
     * Default constructor.
     * 
     * @param id the id
     * @param node the node
     * 
     * @throws NoSuchFieldException reflection error
     * @throws IllegalAccessException reflection error
     */
    public UUIDBreakpointImpl(@Nullable UUID id,
                              @Nullable Node node)
    throws NoSuchFieldException, IllegalAccessException {
        super(node);
        
        //
        // avoid making the id field protected... so we use reflection here
        //
        Field idField = BreakpointImpl.class.getDeclaredField("id");
        idField.setAccessible(true);
        idField.set(this, id);
    }
}
