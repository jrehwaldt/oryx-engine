package org.jodaengine.process.definition;

import java.util.List;
import java.util.UUID;

import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.annotate.JsonTypeInfo;
import org.codehaus.jackson.annotate.JsonTypeInfo.As;
import org.codehaus.jackson.annotate.JsonTypeInfo.Id;

import org.jodaengine.process.structure.Node;
import org.jodaengine.util.Attributable;
import org.jodaengine.util.Identifiable;

/**
 * The Interface of the process definition. The process definition holds the essential information of a process and
 * specifies its list of start nodes.
 * 
 * @author Thorben
 */
@JsonTypeInfo(use = Id.CLASS, include = As.PROPERTY, property = "@classifier")
public interface ProcessDefinition extends Identifiable<UUID>, Attributable {

    /**
     * Retrieves the name of the {@link ProcessDefinition}.
     * 
     * @return the name of the {@link ProcessDefinition}
     */
    @JsonProperty
    String getName();

    /**
     * Sets the name.
     * 
     * @param name
     *            - the new name of the {@link ProcessDefinition}
     */
    void setName(String name);

    /**
     * Gets the description.
     * 
     * @return the description
     */
    @JsonProperty
    String getDescription();

    /**
     * Sets the description.
     * 
     * @param description
     *            the new description of the {@link ProcessDefinition}
     */
    void setDescription(String description);

    /**
     * Gets the start nodes of the process. Tokens can be placed there. As the process definition consists of a tree
     * structure of nodes, this is enough to reference the whole definition.
     * 
     * @return the start nodes
     */
    @JsonProperty
    List<Node> getStartNodes();
}
