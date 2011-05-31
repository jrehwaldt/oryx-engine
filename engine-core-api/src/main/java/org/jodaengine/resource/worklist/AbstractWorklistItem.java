
/*
 * 
 */
package org.jodaengine.resource.worklist;

import java.io.IOException;
import java.util.Set;
import java.util.UUID;

import javax.annotation.Nonnull;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.annotate.JsonTypeInfo;
import org.codehaus.jackson.annotate.JsonTypeInfo.As;
import org.codehaus.jackson.annotate.JsonTypeInfo.Id;
import org.codehaus.jackson.map.ObjectMapper;
import org.jodaengine.forms.Form;
import org.jodaengine.process.token.Token;
import org.jodaengine.resource.AbstractResource;
import org.jodaengine.util.Identifiable;

/**
 * The Interface WorklistItem.
 */
@JsonTypeInfo(use = Id.CLASS, include = As.PROPERTY, property = "@classifier")
public abstract class AbstractWorklistItem implements Identifiable<UUID> {

    /**
     * Gets the status.
     * 
     * @return the status
     */
    @JsonProperty
    public abstract @Nonnull
    WorklistItemState getStatus();

    /**
     * Gets the corresponding {@link Token} that created this {@link AbstractWorklistItem}.
     * 
     * @return the corresponding token
     */
    @JsonIgnore
    public abstract @Nonnull
    Token getCorrespondingToken();

    /**
     * Returns a concrete resource object holding the value of the specified String.
     * The argument is interpreted as json.
     * 
     * @param json
     *            json formated object
     * @return a concrete resource
     * @throws IOException
     *             failed to parse the json
     */
    public static @Nonnull
    AbstractWorklistItem valueOf(@Nonnull String json)
    throws IOException {

        // TODO we do not have access to the ServiceFactory, so we need a new ObjectMapper every time
        return (AbstractWorklistItem) new ObjectMapper().readValue(json, AbstractWorklistItem.class);
    }

    /**
     * Gets the form that shall be filled by a human worker.
     *
     * @return the form
     */
    @JsonIgnore
    public abstract Form getForm();

    /**
     * Gets the assigned resources, so to say the resources that can view and work on this item.
     *
     * @return the assigned resources
     */
    @JsonIgnore
    public abstract Set<AbstractResource<?>> getAssignedResources();
    
    /**
     * Gets the description of the item that provides further information.
     *
     * @return the description
     */
    @JsonProperty
    public abstract String getDescription();
    
    /**
     * Gets the subject of the item that provides a topic.
     *
     * @return the subject
     */
    @JsonProperty
    public abstract String getSubject();
}
