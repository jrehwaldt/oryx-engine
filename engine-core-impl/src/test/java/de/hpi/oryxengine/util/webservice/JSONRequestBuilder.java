package de.hpi.oryxengine.util.webservice;

/**
 * The Class JSONRequestBuilder.
 */
public class JSONRequestBuilder {
    
    /**
     * Instantiates a new jSON request builder.
     *
     * @param participant the participant as JSON
     * @param action the action as JSON
     * @param classifier the classifier as JSON
     * Please look at the call hierarchy for examples.
     */
    public JSONRequestBuilder(String participant, String action, String classifier) {

        this.participant = participant;
        this.action = action;
        this.classifier = classifier;
    }
    
    private String participant;
    private String action;
    private String classifier;

    /**
     * Sets the participant.
     *
     * @param participant the new participant
     * @return the jSON request builder
     */
    public JSONRequestBuilder setParticipant(String participant) {
    
        this.participant = participant;
        return this;
    }

    /**
     * Sets the action.
     *
     * @param action the new action
     * @return the jSON request builder
     */
    public JSONRequestBuilder setAction(String action) {
    
        this.action = action;
        return this;
    }

    /**
     * Sets the classifier.
     *
     * @param classifier the new classifier
     * @return the jSON request builder
     */
    public JSONRequestBuilder setClassifier(String classifier) {
    
        this.classifier = classifier;
        return this;
    }

    /**
     * Builds the string for the JSON request.
     *
     * @return the string
     */
    public String build() {
        return "{"+participant + action + classifier+"}";
    }
    

}
