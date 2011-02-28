package de.hpi.oryxengine.activity.impl;

import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import de.hpi.oryxengine.activity.Activity;
import de.hpi.oryxengine.process.instance.ProcessInstance;

/**
 * The Class MailingVariable. A dummy class by Gerardo which sends Emails with a preprogrammed String and displaying a
 * value from the processinstance.
 */
public class MailingVariable implements Activity {

    /** The host. */
    private static String host = "localhost";

    /** The default sender. */
    private static String defaultSender = "oryxengine@bpt.hpi.uni-potsdam.de";

    /** The default recipient. */
    private static String defaultRecipient = "gns@oryxengine.de";

    /** The subject. */
    private static String subject = "Hier das Ergebnis deiner Berechnung.";

    /** The variable name. */
    private String variableName;

    /**
     * Instantiates a new mailing variable.
     * 
     * @param variableToBeMailed
     *            the variable to be mailed
     */
    public MailingVariable(String variableToBeMailed) {
        super();
        variableName = variableToBeMailed;
    }

    /**
     * Sends an email that contains the Variable which it is given to it in the constructor.
     * 
     * @param instance
     *            the processinstance (to get the variable)
     * @see de.hpi.oryxengine.activity.Activity#execute(de.hpi.oryxengine.process.instance.ProcessInstance)
     */
    public void execute(ProcessInstance instance) {

        String calculationResult = (String) instance.getVariable(variableName);

        String mailBody = "Da Ergebnis der Berechnung von 5 + 5 ist " + calculationResult + ".";
        sendEmail(defaultSender, defaultRecipient, subject, mailBody);
    }

    /**
     * Send a single email.
     * 
     * @param aFromEmailAddr
     *            the sender of the mail
     * @param aToEmailAddr
     *            the address the mail should be send to
     * @param aSubject
     *            the subject of the mail
     * @param aBody
     *            the body of the mail
     */
    public void sendEmail(String aFromEmailAddr, String aToEmailAddr, String aSubject, String aBody) {

        // Here, no Authenticator argument is used (it is null).
        // Authenticators are used to prompt the user for user
        // name and password.
        Session session = Session.getDefaultInstance(getMailServerConfig(), null);
        MimeMessage message = new MimeMessage(session);
        try {
            // the "from" address may be set in code, or set in the
            // config file under "mail.from" ; here, the latter style is used
            message.setFrom(new InternetAddress(aFromEmailAddr));
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(aToEmailAddr));
            message.setSubject(aSubject);
            message.setText(aBody);
            Transport.send(message);
        } catch (MessagingException ex) {
            System.err.println("Cannot send email. " + ex);
        }

    }

    /**
     * Gets the mail server config.
     * 
     * @return the f mail server config
     */
    private Properties getMailServerConfig() {

        Properties properties = new Properties();
        properties.put("mail.smtp.host", host);
        properties.put("mail.smtp.port", "25");

        return properties;
    }

}
