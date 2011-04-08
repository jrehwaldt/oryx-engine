package de.hpi.oryxengine.factory.resource;

import de.hpi.oryxengine.resource.Participant;

/**
 * A little factory for creating {@link AbstractParticipant}s.
 */
public class ParticipantFactory extends ResourceFactory {

    /**
     * Returns a {@link AbstractParticipant} object with the following parameter
     *  id: gerardo.navarro-suarez
     *  name: Gerardo Navarro Suarez
     * 
     * @return the {@link Participant} Gerardo
     */
    public static Participant createGerardo() {

        return createParticipant("gerardo.navarro-suarez", "Gerardo Navarro Suarez");
    }

    /**
     * Returns a {@link AbstractParticipant} object with the the following parameter
     *  id: jannik.streek
     *  name: Jannik Streek
     * 
     * @return the {@link Participant} Jannik
     */
    public static Participant createJannik() {

        return createParticipant("jannik.streek", "Jannik Streek");
    }
    
    /**
     * Returns a {@link AbstractParticipant} object with the the following parameter
     *  id: tobias.pfeiffer
     *  name: Tobias Pfeiffer
     * 
     * @return the {@link Participant} Tobi
     */
    public static Participant createTobi() {

        return createParticipant("tobias.pfeiffer", "Tobias Pfeiffer");
    }
    
    /**
     * Returns a {@link AbstractParticipant} object with the the following parameter
     *  id: tobias.metzke
     *  name: Tobias Metzke
     * 
     * @return the {@link Participant} Tobi2
     */
    public static Participant createTobi2() {

        return createParticipant("tobias.metzke", "Tobias Metzke");
    }
    
    

}
