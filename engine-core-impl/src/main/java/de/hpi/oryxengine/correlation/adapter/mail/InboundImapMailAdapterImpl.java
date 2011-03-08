package de.hpi.oryxengine.correlation.adapter.mail;

import java.io.IOException;

import javax.annotation.Nonnull;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Part;
import javax.mail.Session;
import javax.mail.Store;
import javax.mail.URLName;
import javax.mail.internet.InternetAddress;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.hpi.oryxengine.correlation.CorrelationManager;
import de.hpi.oryxengine.correlation.adapter.PullingInboundAdapter;

/**
 * This is the default imap mail client implementation and acts as {@link CorrelationAdapter} for the
 * {@link CorrelationManager}.
 */
public class InboundImapMailAdapterImpl implements PullingInboundAdapter {
    
    private final Logger logger = LoggerFactory.getLogger(getClass());
    
    private final MailAdapterConfiguration configuration;
    private final MailEvent type;
    
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

    @Override
    public MailEvent getEventType() {
        return this.type;
    }

    @Override
    public @Nonnull
    MailAdapterConfiguration getConfiguration() {
        return this.configuration;
    }

    /**
     * This mailer adapter will receive mails as specified in {@link MailAdapterConfiguration}.
     * 
     * {@inheritDoc}
     * 
     * @throws MessagingException 
     * @throws IOException 
     */
    @Override
    public void pull() throws MessagingException, IOException {
        
        Session session = Session.getInstance(this.configuration.toMailProperties());
        
        logger.debug("getting the session for accessing email.");
        Store store = session.getStore("imap");
        
        store.connect(
            this.configuration.getAddress(),
            this.configuration.getUserName(),
            this.configuration.getPassword()
        );
        logger.debug("Connection established with IMAP server.");
        
        // Get a handle on the default folder
        Folder folder = store.getDefaultFolder();
        
        logger.debug("Getting the Inbox folder.");
        
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
     * @throws MessagingException thrown if handling the message fails
     * @throws IOException thrown if fetching the message content fails
     */
    private void processMessage(@Nonnull Message message)
    throws IOException, MessagingException {
        // TODO Tobi will das hier nicht vergessen.
        this.correlation.correlate(null);
        
        // Retrieve the message content
        Object messageContentObject = message.getContent();
        
        // Determine email type
        if (messageContentObject instanceof Multipart) {
            logger.debug("Found Email with Attachment");
            String sender = ((InternetAddress) message.getFrom()[0]).getPersonal();
            
            // If the "personal" information has no entry, check the address for the sender information
            logger.debug("If the personal information has no entry, check the address for the sender information.");
            
            if (sender == null) {
                sender = ((InternetAddress) message.getFrom()[0]).getAddress();
                logger.debug("sender in NULL. Printing Address:" + sender);
            }
            logger.debug("Sender -." + sender);
            
            // Get the subject information
            String subject = message.getSubject();
            
            logger.debug("subject=" + subject);
            
            // Retrieve the Multipart object from the message
            Multipart multipart = (Multipart) message.getContent();
            
            logger.debug("Retrieve the Multipart object from the message");
            
            // Loop over the parts of the email
            for (int i = 0; i < multipart.getCount(); i++) {
                // Retrieve the next part
                Part part = multipart.getBodyPart(i);
                
                // Get the content type
                String contentType = part.getContentType();
                
                // Display the content type
                logger.debug("Content: " + contentType);
                
                if (contentType.startsWith("text/plain")) {
                    logger.debug("---------reading content type text/plain  mail -------------");
                } else {
                    // Retrieve the file name
                    String fileName = part.getFileName();
                    logger.debug("retrive the fileName=" + fileName);
                }
            }
        } else {
            logger.debug("Found Mail Without Attachment");
            String sender = ((InternetAddress) message.getFrom()[0]).getPersonal();
            
            // If the "personal" information has no entry, check the address for the sender information
            logger.debug("If the personal information has no entry, check the address for the sender information.");
            
            if (sender == null) {
                sender = ((InternetAddress) message.getFrom()[0]).getAddress();
                logger.debug("sender in NULL. Printing Address:" + sender);
            }
            
            // Get the subject information
            String subject = message.getSubject();
            logger.debug("subject=" + subject);
        }
    }
}
