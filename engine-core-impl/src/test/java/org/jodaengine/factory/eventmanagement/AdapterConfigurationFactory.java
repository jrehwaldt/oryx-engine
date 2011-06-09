package org.jodaengine.factory.eventmanagement;

import org.jodaengine.eventmanagement.adapter.mail.IncomingMailAdapterConfiguration;
import org.jodaengine.eventmanagement.adapter.mail.MailProtocol;

/**
 * A factory for creating AdapterConfiguration objects.
 */
public final class AdapterConfigurationFactory {

    private static final MailProtocol MAIL_PROTOCOL = MailProtocol.POP3;
    private static final String USER_NAME = "Test";
    private static final String PASSWORD = "foo";
    private static final String ADDRESS = "me@you.de";
    private static final int PORT = 3000;
    private static final boolean USE_SSL = false;

    /**
     * Utillity class.
     */
    private AdapterConfigurationFactory() {

    }

    /**
     * Creates a new InboundMailAdapterConfiguration with the values given by the appropriate constants.
     * 
     * @return the InboundMailAdapterConfiguration
     */
    public static IncomingMailAdapterConfiguration createMailAdapterConfiguration() {

        return new IncomingMailAdapterConfiguration(MAIL_PROTOCOL, USER_NAME, PASSWORD, ADDRESS, PORT, USE_SSL);
    }

    /**
     * Creates a new slightly different AdapterConfiguration object (other user name than the default
     * createMailAdapterConfiguration method).
     * 
     * @return the inbound mail adapter configuration
     */
    public static IncomingMailAdapterConfiguration createSlightlyDifferentMailAdapterConfiguration() {

        return new IncomingMailAdapterConfiguration(MAIL_PROTOCOL, "Harrie", PASSWORD, ADDRESS, PORT, USE_SSL);
    }

}
