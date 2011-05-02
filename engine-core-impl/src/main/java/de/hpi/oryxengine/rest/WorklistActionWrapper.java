package de.hpi.oryxengine.rest;

import java.io.IOException;
import java.util.UUID;

import javax.annotation.Nonnull;

import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.annotate.JsonTypeInfo;
import org.codehaus.jackson.annotate.JsonTypeInfo.As;
import org.codehaus.jackson.annotate.JsonTypeInfo.Id;
import org.codehaus.jackson.map.ObjectMapper;

/**
 * The Class WrapperObject.
 */
@JsonTypeInfo(use = Id.CLASS, include = As.PROPERTY, property = "@classifier")
public class WorklistActionWrapper {
    
    /**
     * Hidden constructor for deserialization.
     */
    protected WorklistActionWrapper() { }
    
    private WorklistItemAction action;
    private UUID participantId;
    
    /**
     * Gets the action.
     *
     * @return the action
     */
    @JsonProperty("action")
    public WorklistItemAction getAction() {
    
        return action;
    }

    /**
     * Sets the action.
     *
     * @param action the new action
     */
    @JsonProperty("action")
    public void setAction(WorklistItemAction action) {
    
        this.action = action;
    }

    /**
     * Gets the partcipant id.
     *
     * @return the partcipant id
     */
    @JsonProperty("participantId")
    public UUID getParticipantId() {
    
        return participantId;
    }

    /**
     * Sets the partcipant id.
     *
     * @param participantId the new partcipant id
     */
    @JsonProperty("participantId")
    public void setPartcipantId(UUID participantId) {
    
        this.participantId = participantId;
    }

    /**
     * Returns a concrete resource object holding the value of the specified String.
     * The argument is interpreted as json.
     * 
     * @param json json formated object
     * @return a concrete resource
     * @throws IOException failed to parse the json
     */
    public static @Nonnull WorklistActionWrapper valueOf(@Nonnull String json)
    throws IOException {
        System.out.println("valueOf called");
        // TODO we do not have access to the ServiceFactory, so we need a new ObjectMapper every time
        WorklistActionWrapper returnValue = (WorklistActionWrapper) new ObjectMapper().readValue(json, WorklistActionWrapper.class);
        System.out.println("valueOf returning");
        return returnValue;
    }
    

}

