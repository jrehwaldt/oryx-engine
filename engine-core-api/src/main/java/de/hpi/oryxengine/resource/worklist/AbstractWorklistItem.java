/*
 * 
 */
package de.hpi.oryxengine.resource.worklist;

import java.io.IOException;

import javax.annotation.Nonnull;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.annotate.JsonTypeInfo;
import org.codehaus.jackson.annotate.JsonTypeInfo.As;
import org.codehaus.jackson.annotate.JsonTypeInfo.Id;
import org.codehaus.jackson.map.ObjectMapper;

import de.hpi.oryxengine.allocation.Task;
import de.hpi.oryxengine.process.token.Token;
import de.hpi.oryxengine.util.Identifiable;

/**
 * The Interface WorklistItem.
 */
@JsonTypeInfo(use = Id.CLASS, include = As.PROPERTY, property = "classifier")
public abstract class AbstractWorklistItem implements Task, Identifiable {
    
    /**
     * Gets the status.
     *
     * @return the status
     */
    @JsonProperty
    public abstract @Nonnull WorklistItemState getStatus();
    
    /**
     * Gets the corresponding {@link Token} that created this {@link AbstractWorklistItem}.
     *
     * @return the corresponding token
     */
    @JsonIgnore
    public abstract @Nonnull Token getCorrespondingToken();

    
    /**
     * Returns a concrete resource object holding the value of the specified String.
     * The argument is interpreted as json.
     * 
     * @param json json formated object
     * @return a concrete resource
     * @throws IOException failed to parse the json
     */
    public static @Nonnull AbstractWorklistItem valueOf(@Nonnull String json)
    throws IOException {
        // TODO we do not have access to the ServiceFactory, so we need a new ObjectMapper every time
        System.out.println("looool");
        System.out.println("looool");
        System.out.println("looool");
        System.out.println("looool: " + json);
        return (AbstractWorklistItem) new ObjectMapper().readValue(json, AbstractWorklistItem.class);
    }
}
