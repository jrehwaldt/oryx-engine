package org.jodaengine.process.definition;

import java.util.UUID;

import org.codehaus.jackson.annotate.JsonCreator;
import org.codehaus.jackson.annotate.JsonProperty;

/**
 * A ProcessDefinitionID is made up of a UUID that identifies the process itself and a version number.
 */
public class ProcessDefinitionID {

    private UUID uuid;
    private int version;
    private static final String DELIMITER = ":";

    /**
     * Instantiates a new process definition id.
     * 
     * @param uuid
     *            the uuid
     * @param version
     *            the version
     */
    @JsonCreator
    public ProcessDefinitionID(@JsonProperty("uuid") UUID uuid, @JsonProperty("version") int version) {

        this.uuid = uuid;
        this.version = version;
    }

    /**
     * Convenience constructor for IDs with version 0.
     * 
     * @param uuid
     *            the uuid
     */
    public ProcessDefinitionID(UUID uuid) {

        this(uuid, 0);
    }

    /**
     * Gets the UUID.
     * 
     * @return the uUID
     */
    @JsonProperty
    public UUID getUUID() {

        return uuid;
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
        if (uuid == null) {
            result = prime * result;
        } else {
            result = prime * result + uuid.hashCode();
        }
        result = prime * result + version;
        return result;
    }

    @Override
    public boolean equals(Object obj) {

        if (obj instanceof ProcessDefinitionID) {
            ProcessDefinitionID anotherID = (ProcessDefinitionID) obj;

            if (anotherID.getUUID().equals(uuid) && anotherID.getVersion() == version) {
                return true;
            }
        }
        return false;
    }

    @Override
    public String toString() {

        return uuid.toString() + DELIMITER + version;
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
        UUID uuid = UUID.fromString(id.subSequence(0, delimiterPosition).toString());
        int version = new Integer(id.substring(delimiterPosition + 1));
        return new ProcessDefinitionID(uuid, version);

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
