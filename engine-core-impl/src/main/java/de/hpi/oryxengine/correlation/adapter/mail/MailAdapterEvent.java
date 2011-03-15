package de.hpi.oryxengine.correlation.adapter.mail;

import java.io.IOException;
import java.util.Date;

import javax.annotation.Nonnull;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Part;
import javax.mail.internet.InternetAddress;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.hpi.oryxengine.correlation.AdapterEvent;
import de.hpi.oryxengine.correlation.EventType;

/**
 * A event implementation for the mail adapter.
 */
public class MailAdapterEvent implements AdapterEvent {
    
    private final Logger logger = LoggerFactory.getLogger(getClass());
    
    private EventType type;
    private Message message;
    private String content;
    private String topic;
    private String from;
    
    /**
     * Default constructor.
     * 
     * @param type the adapter's event type.
     * @param message the email object
     * @throws MessagingException thrown if parseContent throws exception
     * @throws IOException thrown if parseContent throws exception
     */
    MailAdapterEvent(@Nonnull EventType type,
                     @Nonnull Message message) throws IOException, MessagingException {
        this.type = type;
        this.message = message;
        parseContent();
    }
    
    /**
     * Parse the mail content and save the results.
     * 
     * @throws MessagingException thrown if getting context fails
     * @throws IOException thrown if getting context fails
     */
    public void parseContent() throws IOException, MessagingException {
        // Retrieve the this.message content
        Object messageContentObject = this.message.getContent();
        
        // Determine email type
        if (messageContentObject instanceof Multipart) {
            logger.debug("Found Email with Attachment");
            String sender = ((InternetAddress) this.message.getFrom()[0]).getPersonal();
            this.from = sender;
            
            // If the "personal" information has no entry, check the address for the sender information
            logger.debug("If the personal information has no entry, check the address for the sender information.");
            
            if (sender == null) {
                sender = ((InternetAddress) this.message.getFrom()[0]).getAddress();
                logger.debug("sender in NULL. Printing Address:" + sender);
            }
            logger.debug("Sender -." + sender);
            
            // Get the subject information
            String subject = this.message.getSubject();
            this.topic = subject;
            
            logger.debug("subject=" + subject);
            
            // Retrieve the Multipart object from the this.message
            Multipart multipart = (Multipart) this.message.getContent();
            
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
                    this.content = (String) part.getContent();
                } else {
                    // Retrieve the file name
                    String fileName = part.getFileName();
                    logger.debug("retrive the fileName=" + fileName);
                }
                
                
            }
        } else {
            logger.debug("Found Mail Without Attachment");
            String sender = ((InternetAddress) this.message.getFrom()[0]).getPersonal();
            
            // If the "personal" information has no entry, check the address for the sender information
            logger.debug("If the personal information has no entry, check the address for the sender information.");
            
            if (sender == null) {
                sender = ((InternetAddress) this.message.getFrom()[0]).getAddress();
                logger.debug("sender in NULL. Printing Address:" + sender);
            }
            
            // Get the subject information
            String subject = this.message.getSubject();
            logger.debug("subject=" + subject);
            
        }
        
        
    }
    
    /**
     * Get the E-Mail content.
     * 
     * @return the mail content.
     */
    public String getMessageContent() {
        return this.content;
      
    }
    
    /**
     * Get the E-Mail topic.
     * 
     * @return the mail topic.
     */
    public String getMessageTopic() {
        return this.topic;
      
    }
    
    /**
     * Get the E-Mail sender.
     * 
     * @return the mail sender.
     */
    public String getMessageFrom() {
        return this.from;
      
    }
    
    @Override
    public EventType getEventType() {
        return this.type;
    }
    
    @Override
    public Date getTimestamp() {
        return null;
    }
}
