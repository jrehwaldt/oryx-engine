package de.hpi.oryxengine.resource;

/**
 * A capability is some desired skill or ability possessed by a participant.Examples would be first aid skills, health
 * and safety training, etc. .
 * 
 * Participants that possess special abilities could be considered by certain resource allocations.
 * 
 */
public class Capability extends AbstractResource<Capability> {
    
    // TODO @Gerardo: wieso ist Capability mal implemenmtiert und mal nicht? MAAAAAAAAAAAAAAAAAANNNNNN!!
    // FROM Gerardo: Weil das noch nicht fertig war 
    
    /**
     * The Default Constructor for the CapabilityImpl.
     * 
     * @param capabilityId - the id of the capability
     */
    public Capability(String capabilityId) {

       super(capabilityId, ResourceType.CAPABILITY);
    }

}
