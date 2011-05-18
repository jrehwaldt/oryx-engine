package org.jodaengine.process.definition;

import java.util.UUID;

/**
 * A ProcessDefinitionID is made up of a UUID that identifies the process itself and a version number.
 */
public class ProcessDefinitionID {

    private UUID uuid;
    private int version;

    /**
     * Instantiates a new process definition id.
     * 
     * @param uuid
     *            the uuid
     * @param version
     *            the version
     */
    public ProcessDefinitionID(UUID uuid, int version) {

        this.uuid = uuid;
        this.version = version;
    }

    /**
     * Gets the UUID.
     * 
     * @return the uUID
     */
    public UUID getUUID() {

        return uuid;
    }

    /**
     * Gets the version number of this process definition.
     * 
     * @return the version
     */
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

}
