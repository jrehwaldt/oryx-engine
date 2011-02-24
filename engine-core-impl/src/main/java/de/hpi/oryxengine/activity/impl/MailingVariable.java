package de.hpi.oryxengine.activity.impl;

import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import de.hpi.oryxengine.activity.Activity;
import de.hpi.oryxengine.processInstance.ProcessInstance;

public class MailingVariable implements Activity {

    private static String host = "localhost";
    private static String defaultSender = "oryxengine@bpt.hpi.uni-potsdam.de";
    private static String defaultRecipient = "gns@oryxengine.de";
    private static String subject = "Hier das Ergebnis deiner Berechnung.";

    private String variableName;

    public MailingVariable(String variableToBeMailed) {

        variableName = variableToBeMailed;
    }

    public void execute(ProcessInstance instance) {

        String calculationResult = (String) instance.getVariable(variableName);

        String mailBody = "Da Ergebnis der Berechnung von 5 + 5 ist " + calculationResult + ".";
        sendEmail(defaultSender, defaultRecipient, subject, mailBody);
    }

    /**
     * Send a single email.
     *
     * @param aFromEmailAddr the sender of the mail
     * @param aToEmailAddr the address the mail should be send to
     * @param aSubject the subject of the mail
     * @param aBody the body of the mail
     */
    public void sendEmail(String aFromEmailAddr, String aToEmailAddr, String aSubject, String aBody) {

        // Here, no Authenticator argument is used (it is null).
        // Authenticators are used to prompt the user for user
        // name and password.
        Session session = Session.getDefaultInstance(getfMailServerConfig(), null);
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

    private Properties getfMailServerConfig() {

        Properties properties = new Properties();
        properties.put("mail.smtp.host", host);
        properties.put("mail.smtp.port", "25");

        return properties;
    }

}
