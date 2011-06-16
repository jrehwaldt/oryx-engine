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
import org.jodaengine.eventmanagement.adapter.configuration.AdapterConfiguration;
import org.jodaengine.exception.JodaEngineRuntimeException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A event implementation for the mail adapter.
 */
public class MailAdapterEvent extends AbstractAdapterEvent {

    private final Logger logger = LoggerFactory.getLogger(getClass());
    private @Nonnull
    Message message;
    private @Nullable
    String content;
    private @Nullable
    String subject;
    private @Nullable
    String from;

    /**
     * Default constructor.
     * 
     * @param configuration
     *            the adapter's {@link AdapterConfiguration}.
     * @param message
     *            the email {@link Message} object
     */
    public MailAdapterEvent(@Nonnull AdapterConfiguration configuration, @Nonnull Message message) {

        super(configuration);
        this.message = message;
        parseEmail();
    }

    /**
     * Parse the mail content and save the results.
     */
    public void parseEmail() {

        try {
            parseSender();
            parseSubject();
            parseContent();
        } catch (IOException ioException) {

            String errorMessage = "The following exception occurred: " + ioException.getMessage();
            logger.error(errorMessage, ioException);
            throw new JodaEngineRuntimeException(errorMessage, ioException);

        } catch (MessagingException messageException) {

            String errorMessage = "The following exception occurred: " + messageException.getMessage();
            logger.error(errorMessage, messageException);
            throw new JodaEngineRuntimeException(errorMessage, messageException);
        }
    }

    /**
     * Retrieve the message content.
     * 
     * @throws IOException
     *             Signals that an I/O exception has occurred.
     * @throws MessagingException
     *             the messaging exception
     */
    private void parseContent()
    throws IOException, MessagingException {

        Object messageContentObject = this.message.getContent();
        // Determine email type
        logger.debug("message Content object is of class:" + messageContentObject.getClass());
        if (messageContentObject instanceof String) {
            this.content = (String) messageContentObject;

        } else if (messageContentObject instanceof Multipart) {
            parseMultipartMessage();
        } else {
            // unknown email content (like com.sun.mail.imap.IMAPInputStream;)
            logger.error("The email content is of an unknown class, this class being: "
                + messageContentObject.getClass());
        }
    }

    /**
     * Parsing a Multipart type Email.
     * 
     * @throws IOException
     *             Signals that an I/O exception has occurred.
     * @throws MessagingException
     *             the messaging exception
     */
    private void parseMultipartMessage()
    throws IOException, MessagingException {

        // Retrieve the Multipart object from the this.message
        Multipart multipart = (Multipart) this.message.getContent();

        // Loop over the parts of the email
        for (int i = 0; i < multipart.getCount(); i++) {
            // Retrieve the next part
            Part part = multipart.getBodyPart(i);

            // Get the content type
            String contentType = part.getContentType();

            // TobiBA: Support for more content types goes here!
            if (contentType.toLowerCase().startsWith("text/plain")) {
                this.content = (String) part.getContent();
            } else {
                // Retrieve the file name
                String fileName = part.getFileName();
                logger.debug("retrive the fileName=" + fileName);
            }

        }
    }

    /**
     * Get the subject information.
     * 
     * @throws MessagingException
     *             the messaging exception
     */
    private void parseSubject()
    throws MessagingException {

        this.subject = this.message.getSubject();
    }

    /**
     * Get the sender information.
     * 
     * @throws MessagingException
     *             the messaging exception
     */
    private void parseSender()
    throws MessagingException {

        String sender = ((InternetAddress) this.message.getFrom()[0]).getPersonal();

        if (sender == null) {
            sender = ((InternetAddress) this.message.getFrom()[0]).getAddress();
        }

        this.from = sender;
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
    String getMessageSubject() {

        return this.subject;

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
