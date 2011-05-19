package org.jodaengine.eventmanagement.adapter.mail;

import java.io.IOException;

import javax.annotation.Nonnull;
import javax.mail.Flags.Flag;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Store;

import org.jodaengine.eventmanagement.CorrelationManager;
import org.jodaengine.eventmanagement.adapter.AbstractCorrelatingEventAdapter;
import org.jodaengine.eventmanagement.adapter.CorrelationAdapter;
import org.jodaengine.eventmanagement.adapter.InboundPullAdapter;
import org.jodaengine.exception.JodaEngineException;

/**
 * This is the default imap mail client implementation and acts as {@link CorrelationAdapter} for the.
 * 
 * {@link CorrelationManager}.
 */
public class InboundImapMailAdapter extends AbstractCorrelatingEventAdapter<InboundMailAdapterConfiguration>
implements InboundPullAdapter {

    /**
     * Default constructor.
     * 
     * @param configuration
     *            the adapter's configuration
     */
    @SuppressWarnings("restriction")
    public InboundImapMailAdapter(@Nonnull InboundMailAdapterConfiguration configuration) {

        super(configuration);

        this.logger.info("MailAdapter initialized with config: {}", this.configuration);

        if (this.configuration.isUseSSL()) {
            java.security.Security.addProvider(new com.sun.net.ssl.internal.ssl.Provider());
        }
    }

    /**
     * This mailer adapter will receive mails as specified in {@link InboundMailAdapterConfiguration}.
     * 
     * {@inheritDoc}
     */
    @Override
    public void pull()
    throws JodaEngineException {

        try {
            Session session = Session.getInstance(this.configuration.toMailProperties());

            Store store = session.getStore("imap");

            store.connect(this.configuration.getAddress(), this.configuration.getUserName(),
                this.configuration.getPassword());

            // Get a handle on the default folder
            Folder folder = store.getDefaultFolder();

            // Retrieve the "Inbox"
            folder = folder.getFolder("inbox");

            // Reading the Email Index in Read / Write Mode
            folder.open(Folder.READ_WRITE);

            // Retrieve the messages
            Message[] messages = folder.getMessages();

            logger.debug("Found {} messages", messages.length);

            // Loop over all of the messages. Only process them if they are new.
            for (Message message : messages) {
                if (!message.isSet(Flag.SEEN)) {
                    processMessage(message);
                }
            }

            // Close the folder
            folder.close(true);

            // Close the message store
            store.close();
        } catch (MessagingException me) {
            throw new JodaEngineException("Processing the server's messages failed.", me);
        } catch (IOException ioe) {
            throw new JodaEngineException("Communication error during mail fetching.", ioe);
        }
    }

    /**
     * Called for message processing.
     * 
     * @param message
     *            a message to process
     * @throws IOException
     *             thrown if fetching the message content fails
     * @throws MessagingException
     *             thrown if handling the message fails
     */
    private void processMessage(@Nonnull Message message)
    throws IOException, MessagingException {

        correlateAdapterEvent(new MailAdapterEvent(this.configuration, message));
    }
}
