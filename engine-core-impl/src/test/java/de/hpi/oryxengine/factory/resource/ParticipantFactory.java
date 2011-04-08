package de.hpi.oryxengine.factory.resource;

import de.hpi.oryxengine.resource.AbstractParticipant;

/**
 * A little factory for creating {@link AbstractParticipant}s.
 */
public class ParticipantFactory extends ResourceFactory {

    /**
     * Returns a {@link AbstractParticipant} object with the following parameter
     *  id: gerardo.navarro-suarez
     *  name: Gerardo Navarro Suarez
     * 
     * @return the {@link AbstractParticipant} Gerardo
     */
    public static AbstractParticipant createGerardo() {

        return createParticipant("gerardo.navarro-suarez", "Gerardo Navarro Suarez");
    }

    /**
     * Returns a {@link AbstractParticipant} object with the the following parameter
     *  id: jannik.streek
     *  name: Jannik Streek
     * 
     * @return the {@link AbstractParticipant} Jannik
     */
    public static AbstractParticipant createJannik() {

        return createParticipant("jannik.streek", "Jannik Streek");
    }

}
