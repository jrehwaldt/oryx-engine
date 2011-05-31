package org.jodaengine.ext.debugging.util;

import java.util.List;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.jodaengine.process.definition.ProcessDefinitionID;
import org.jodaengine.process.definition.ProcessDefinitionImpl;
import org.jodaengine.process.structure.Node;

/**
 * This is a testing implementation, which allows to set the {@link ProcessDefinitionID} of
 * a {@link ProcessDefinitionImpl} explicitly.
 * 
 * @author Jan Rehwaldt
 * @since 2011-05-31
 */
public class IDProcessDefinitionImpl extends ProcessDefinitionImpl {
    
    /**
     * Default constructor.
     * 
     * @param id the id to set
     * @param nodes the start nodes
     */
    public IDProcessDefinitionImpl(@Nullable ProcessDefinitionID id,
                                   @Nonnull List<Node> nodes) {
        super(id, null, null, nodes);
    }
}
