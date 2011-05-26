package org.jodaengine.process.definition;

import org.codehaus.jackson.annotate.JsonCreator;
import org.codehaus.jackson.annotate.JsonProperty;

/**
 * A ProcessDefinitionID is made up of a UUID that identifies the process itself and a version number.
 */
public class ProcessDefinitionID {

    private String identifier;
    private int version;
    private static final String DELIMITER = ":";

    /**
     * Instantiates a new process definition id.
     *
     * @param identifier the identifier
     * @param version the version
     */
    @JsonCreator
    public ProcessDefinitionID(@JsonProperty("identifier") String identifier, @JsonProperty("version") int version) {

        this.identifier = identifier;
        this.version = version;
    }

    /**
     * Convenience constructor for IDs with version 0.
     *
     * @param identifier the identifier
     */
    public ProcessDefinitionID(String identifier) {

        this(identifier, 0);
    }

    /**
     * Gets the Identifier of this process.
     * 
     * @return the identifier
     */
    @JsonProperty
    public String getIdentifier() {

        return identifier;
    }

    /**
     * Gets the version number of this process definition.
     * 
     * @return the version
     */
    @JsonProperty
    public int getVersion() {

        return version;
    }

    @Override
    public int hashCode() {

        final int prime = 31;
        int result = 1;
        if (identifier == null) {
            result = prime * result;
        } else {
            result = prime * result + identifier.hashCode();
        }
        result = prime * result + version;
        return result;
    }

    @Override
    public boolean equals(Object obj) {

        if (obj instanceof ProcessDefinitionID) {
            ProcessDefinitionID anotherID = (ProcessDefinitionID) obj;

            if (anotherID.getIdentifier().equals(identifier) && anotherID.getVersion() == version) {
                return true;
            }
        }
        return false;
    }

    @Override
    public String toString() {

        return identifier + DELIMITER + version;
    }

    /**
     * Reads a ProcessDefinitionID from a given String.
     * 
     * @param id
     *            the id
     * @return the process definition id
     */
    public static ProcessDefinitionID fromString(String id) {

        int delimiterPosition = id.lastIndexOf(DELIMITER);
        String identifier = id.subSequence(0, delimiterPosition).toString();
        int version = new Integer(id.substring(delimiterPosition + 1));
        return new ProcessDefinitionID(identifier, version);

    }

    /**
     * Sets the version of the ID. This should be possible, as a new version number may be assigned to a process
     * definition.
     * 
     * @param newVersion
     *            the new version
     */
    public void setVersion(int newVersion) {

        this.version = newVersion;
    }

}
