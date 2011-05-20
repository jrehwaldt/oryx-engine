package org.jodaengine.eventmanagement.adapter.mail;

import org.jodaengine.eventmanagement.AdapterEvent;
import org.jodaengine.exception.JodaEngineRuntimeException;

/**
 * Only a little mock for testing whether method correlate was called or not.
 */
public class InboundImapMailAdapterMock extends InboundImapMailAdapter {

    private boolean correlateMethodInvoked;
    private MailAdapterEvent mailAdapterEventArgument;

    /**
     * Default constructor. Does nothing but forwarding.
     * 
     * @param configuration
     *            - the adapter's configuration
     */
    public InboundImapMailAdapterMock(InboundMailAdapterConfiguration configuration) {

        super(configuration);
        this.correlateMethodInvoked = false;
    }

    /**
     * Mocking the method correlate. {@inheritDoc}
     */
    @Override
    public void correlate(AdapterEvent e) {

        this.correlateMethodInvoked = true;
        this.mailAdapterEventArgument = (MailAdapterEvent) e;
    }

    /**
     * Returns true if the method 'correlate' was called.
     * 
     * @return true if the method 'correlate' was called
     */
    public boolean correlateMethodInvoked() {

        return correlateMethodInvoked;
    }

    /**
     * Returns the argument, a {@link MailAdapterEvent}, passed in the correlate method.
     * 
     * @return the {@link MailAdapterEvent} passed in the correlate method
     */
    public MailAdapterEvent getMailAdapterEventArgument() {

        if (mailAdapterEventArgument == null) {
            String errorMessage = "The method 'correlate' was not invoked on this MailAdapterEvent.";
            throw new JodaEngineRuntimeException(errorMessage);
        }
        return mailAdapterEventArgument;
    }
}
