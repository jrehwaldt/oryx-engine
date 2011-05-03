package de.hpi.oryxengine.rest.api;

import de.hpi.oryxengine.ServiceFactory;
import de.hpi.oryxengine.resource.IdentityBuilder;

/**
 * The Class DemoDataForWebservice generates some example data when called.
 */
public final class LoadDemoProcessAsXmlForWebservice {

    private static IdentityBuilder builder;
    private static boolean invoked = false;

    /**
     * singleton.
     */
    private LoadDemoProcessAsXmlForWebservice() {
        
    }

    /**
     * Resets invoked, to be honest mostly for testign purposed after each method.
     */
    public synchronized static void resetInvoked() {

        invoked = false;
    }

    /**
     * Gets the builder.
     * 
     * @return the builder
     */
    private static IdentityBuilder getBuilder() {

        builder = ServiceFactory.getIdentityService().getIdentityBuilder();
        return builder;
    }

    /**
     * Generate example Participants.
     */
    public static synchronized void generate() {

        if (!invoked) {
            invoked = true;
            generateDemoParticipants();
        }

    }

    /**
     * Generate demo participants.
     */
    private static void generateDemoParticipants() {

        getBuilder().createParticipant("Thorben");
    }
}
