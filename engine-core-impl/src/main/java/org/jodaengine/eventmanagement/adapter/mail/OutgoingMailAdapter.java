package org.jodaengine.eventmanagement.adapter.mail;

import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.jodaengine.eventmanagement.adapter.AbstractEventAdapter;
import org.jodaengine.eventmanagement.adapter.outgoing.OutgoingAdapter;
import org.jodaengine.exception.JodaEngineRuntimeException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * An Adapter for sending emails via the SMTP protocol.
 */
public class OutgoingMailAdapter extends AbstractEventAdapter<OutgoingMailAdapterConfiguration> 
    implements OutgoingAdapter {

    // TODO @EVENTTEAM Usage of USERNAME and PASSWORD missing
    public final static String DEFAULT_SUBJECT = "JodaMail, you have!";
    private final Logger logger = LoggerFactory.getLogger(getClass());
    
    /**
     * Create a new outgoing MailAdapter from its configuration.
     *
     * @param configuration the configuration
     */
    public OutgoingMailAdapter(OutgoingMailAdapterConfiguration configuration) {
        super(configuration);
    }

    @Override
    public void sendMessage(String receipent, String message) {
        sendMessage(receipent, DEFAULT_SUBJECT, message);
    }

    @Override
    public void sendMessage(String receipent, String subject, String message) {

        String[] receipents = {receipent};
        this.sendEmail(receipents, subject, message);
        
    }
    
    
    /**
     * Send an email.
     *
     * @param recipients the recipients of the email
     * @param subject the subject of the email
     * @param message the message to be send as an email
     */
    private void sendEmail(String[] recipients, String subject, String message)
    {
        try {
             //Set the SMTP address
             Properties props = new Properties();
             props.put("mail.smtp.host", this.configuration.getSmtpServer());
    
            // create some properties and get the default Session
            Session session = Session.getDefaultInstance(props, null);
            
            // at this point you may setDebug if neded.
    
            Message msg = new MimeMessage(session);
    
            // set the from and to address
            InternetAddress addressFrom = new InternetAddress(this.configuration.getEmailAddress());
                msg.setFrom(addressFrom);
            
            // An email may have multiple recipients
            InternetAddress[] addressTo = new InternetAddress[recipients.length]; 
            for (int i = 0; i < recipients.length; i++) {
                addressTo[i] = new InternetAddress(recipients[i]);
            }
            msg.setRecipients(Message.RecipientType.TO, addressTo);
           
            // Setting the Subject and Content Type
            msg.setSubject(subject);
            msg.setContent(message, "text/plain");
            Transport.send(msg);
        } catch (MessagingException e) {
            String errorMessage = "A messaging exception occured! Something is wrong with the mail you tried to send!";
            logger.error(errorMessage, e);
            throw new JodaEngineRuntimeException(errorMessage, e);
        }

    }
}
