/*
 * 
 */
package org.jodaengine.resource.worklist;

import java.io.IOException;
import java.util.Set;

import javax.annotation.Nonnull;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.annotate.JsonTypeInfo;
import org.codehaus.jackson.annotate.JsonTypeInfo.As;
import org.codehaus.jackson.annotate.JsonTypeInfo.Id;
import org.codehaus.jackson.map.ObjectMapper;

import org.jodaengine.allocation.Form;
import org.jodaengine.process.token.Token;
import org.jodaengine.resource.AbstractResource;
import org.jodaengine.util.Identifiable;

/**
 * The Interface WorklistItem.
 */
// TODO worklistitem has a task?
@JsonTypeInfo(use = Id.CLASS, include = As.PROPERTY, property = "@classifier")
public abstract class AbstractWorklistItem implements Identifiable {

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

    @JsonIgnore
    public Form getForm() {

        // TODO Auto-generated method stub
        return null;
    }

    @JsonIgnore
    public Set<AbstractResource<?>> getAssignedResources() {

        // TODO Auto-generated method stub
        return null;
    }

    @JsonProperty
    public String getDescription() {

        // TODO Auto-generated method stub
        return null;
    }

    @JsonProperty
    public String getSubject() {

        // TODO Auto-generated method stub
        return null;
    }

    // @JsonIgnore
    // public abstract @Nonnull Form getForm();
    //
    // @JsonIgnore
    // public abstract @Nonnull Set<AbstractResource<?>> getAssignedResources();
    //
    // @JsonProperty
    // public abstract @Nonnull String getDescription();
    //
    // @JsonProperty
    // public abstract @Nonnull String getSubject();
}
