package de.hpi.oryxengine.correlation.adapter.mail;

import java.io.IOException;

import javax.annotation.Nonnull;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Store;

import de.hpi.oryxengine.correlation.CorrelationManager;
import de.hpi.oryxengine.correlation.adapter.AbstractCorrelationAdapter;
import de.hpi.oryxengine.correlation.adapter.InboundPullAdapter;
import de.hpi.oryxengine.exception.DalmatinaException;

/**
 * This is the default imap mail client implementation and acts as {@link CorrelationAdapter} for the.
 *
 * {@link CorrelationManager}.
 */
public class InboundImapMailAdapterImpl
extends AbstractCorrelationAdapter<MailAdapterConfiguration>
implements InboundPullAdapter {
    
    /**
     * Default constructor.
     * 
     * @param correlation
     *            the correlation manager
     * @param configuration
     *            the adapter's configuration
     */
    @SuppressWarnings("restriction")
    public InboundImapMailAdapterImpl(@Nonnull CorrelationManager correlation,
                                      @Nonnull MailAdapterConfiguration configuration) {
        super(correlation, configuration);
        
        this.logger.info("MailAdapter initialized with config: {}", this.configuration);
        
        if (this.configuration.isUseSSL()) {
            java.security.Security.addProvider(
                new com.sun.net.ssl.internal.ssl.Provider()
             );
        }
    }

    /**
     * This mailer adapter will receive mails as specified in {@link MailAdapterConfiguration}.
     * 
     * {@inheritDoc}
     */
    @Override
    public void pull()
    throws DalmatinaException {
        
        try {
            Session session = Session.getInstance(this.configuration.toMailProperties());
            
    //        logger.debug("getting the session for accessing email.");
            Store store = session.getStore("imap");
            
            store.connect(
                this.configuration.getAddress(),
                this.configuration.getUserName(),
                this.configuration.getPassword()
            );
    //        logger.debug("Connection established with IMAP server.");
            
            // Get a handle on the default folder
            Folder folder = store.getDefaultFolder();
            
    //        logger.debug("Getting the Inbox folder.");
            
            // Retrieve the "Inbox"
            folder = folder.getFolder("inbox");
            
            // Reading the Email Index in Read / Write Mode
            folder.open(Folder.READ_WRITE);
            
            // Retrieve the messages
            Message[] messages = folder.getMessages();
            
            logger.debug("Found {} messages", messages.length);
            
            // Loop over all of the messages
            for (Message message: messages) {
                processMessage(message);
            }
            
            // Close the folder
            folder.close(true);
            
            // Close the message store
            store.close();
        } catch (MessagingException me) {
            throw new DalmatinaException("Processing the server's messages failed.", me);
        } catch (IOException ioe) {
            throw new DalmatinaException("Communication error during mail fetching.", ioe);
        }
    }

    /**
     * Called for message processing.
     *
     * @param message a message to process
     * @throws IOException thrown if fetching the message content fails
     * @throws MessagingException thrown if handling the message fails
     */
    private void processMessage(@Nonnull Message message)
    throws IOException, MessagingException {
        correlate(new MailAdapterEvent(this.configuration, message));
    }
}
