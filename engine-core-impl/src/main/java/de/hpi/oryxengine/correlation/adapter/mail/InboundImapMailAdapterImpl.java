package de.hpi.oryxengine.correlation.adapter.mail;

import java.io.IOException;

import javax.annotation.Nonnull;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Store;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.hpi.oryxengine.correlation.CorrelationManager;
import de.hpi.oryxengine.correlation.adapter.PullingInboundAdapter;

/**
 * This is the default imap mail client implementation and acts as {@link CorrelationAdapter} for the.
 *
 * {@link CorrelationManager}.
 */
public class InboundImapMailAdapterImpl implements PullingInboundAdapter {
    
    /** The logger. */
    private final Logger logger = LoggerFactory.getLogger(getClass());
    
    /** The configuration. */
    private final MailAdapterConfiguration configuration;
    
    /** The type. */
    private final MailEvent type;
    
    /** The correlation. */
    private final CorrelationManager correlation;
    
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
        this.correlation = correlation;
        this.configuration = configuration;
        this.type = new MailEvent();
        logger.info("MailAdapter initialized with config: {}", this.configuration);
        
        if (this.configuration.isUseSSL()) {
            java.security.Security.addProvider(
                new com.sun.net.ssl.internal.ssl.Provider()
             );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public MailEvent getEventType() {
        return this.type;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public @Nonnull
    MailAdapterConfiguration getConfiguration() {
        return this.configuration;
    }

    /**
     * This mailer adapter will receive mails as specified in {@link MailAdapterConfiguration}.
     *
     * @throws MessagingException the messaging exception
     * @throws IOException Signals that an I/O exception has occurred.
     * {@inheritDoc}
     */
    @Override
    public void pull() throws MessagingException, IOException {
        
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
        
        // Loop over all of the messages
        for (Message message: messages) {
            processMessage(message);
        }
        
        // Close the folder
        folder.close(true);
        
        // Close the message store
        store.close();
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
        this.correlation.correlate(new MailAdapterEvent(getEventType(), message));
    }
}
