package org.jodaengine.eventmanagement.adapter.mail;

import java.io.IOException;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Part;
import javax.mail.internet.InternetAddress;

import org.jodaengine.eventmanagement.adapter.AbstractAdapterEvent;
import org.jodaengine.eventmanagement.adapter.AdapterConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * A event implementation for the mail adapter.
 */
public class MailAdapterEvent extends AbstractAdapterEvent {

    private final Logger logger = LoggerFactory.getLogger(getClass());
    private @Nonnull Message message;
    private @Nullable String content;
    private @Nullable String topic;
    private @Nullable String from;

    /**
     * Default constructor.
     * 
     * @param configuration
     *            the adapter's {@link AdapterConfiguration}.
     * @param message
     *            the email {@link Message} object
     * @throws MessagingException
     *             thrown if parseContent throws exception
     * @throws IOException
     *             thrown if parseContent throws exception
     */
    public MailAdapterEvent(@Nonnull AdapterConfiguration configuration, @Nonnull Message message)
    throws IOException, MessagingException {

        super(configuration);
        this.message = message;
        parseContent();
    }

    /**
     * Parse the mail content and save the results.
     * 
     * @throws MessagingException
     *             thrown if getting context fails
     * @throws IOException
     *             thrown if getting context fails
     */
    public void parseContent()
    throws IOException, MessagingException {

        // Retrieve the this.message content
        Object messageContentObject = this.message.getContent();
        String sender = ((InternetAddress) this.message.getFrom()[0]).getPersonal();

        this.from = sender;
        if (sender == null) {
            sender = ((InternetAddress) this.message.getFrom()[0]).getAddress();
            logger.debug("sender in NULL. Printing Address:" + sender);
        }

        // Get the subject information
        String subject = this.message.getSubject();
        this.topic = subject;

        // Determine email type
        if (messageContentObject instanceof Multipart) {
            logger.debug("Found Email with Attachment");

            // If the "personal" information has no entry, check the address for the sender information
            logger.debug("If the personal information has no entry, check the address for the sender information.");

            logger.debug("Sender -." + sender);

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
            logger.debug("subject=" + subject);

        }

    }

    /**
     * Get the E-Mail content.
     * 
     * @return the mail content.
     */
    public @Nullable
    String getMessageContent() {

        return this.content;

    }

    /**
     * Get the E-Mail topic.
     * 
     * @return the mail topic.
     */
    public @Nullable
    String getMessageTopic() {

        return this.topic;

    }

    /**
     * Get the E-Mail sender.
     * 
     * @return the mail sender.
     */
    public @Nullable
    String getMessageFrom() {

        return this.from;

    }
}
