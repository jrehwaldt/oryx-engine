package org.jodaengine.rest.demo;

import org.jodaengine.ServiceFactory;
import org.jodaengine.resource.IdentityBuilder;

/**
 * The Class DemoDataForWebservice generates some example data when called.
 */
public final class LoadDemoProcessAsXmlForWebservice {

    private static IdentityBuilder builder;
    private static boolean invoked = false;

    /**
     * Hidden singleton constructor.
     */
    private LoadDemoProcessAsXmlForWebservice() {

    }

    /**
     * Resets invoked, to be honest mostly for testing purposed after each method.
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
