package de.hpi.oryxengine.activity.impl;

import java.awt.image.RescaleOp;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.naming.spi.DirStateFactory.Result;

import de.hpi.oryxengine.activity.Activity;
import de.hpi.oryxengine.processInstance.ProcessInstance;

public class MailingVariable implements Activity {

  private static String host = "smtp.mail.yahoo.com";
  private static String defaultSender = "gerar_1989@yahoo.de";
  private static String defaultRecipient = "schokoladenvakuumbombe@hotmail.de";
  private static String subject = "Hier das Ergebnis deiner Berechnung.";

  private String varibaleName;

  public MailingVariable(String variableToBeMailed) {
    varibaleName = variableToBeMailed;
  }

  public void execute(ProcessInstance instance) {
    String calculationResult = (String) instance.getVariable(varibaleName);

    String mailBody = "Da Ergebnis der Berechnung von 5 + 5 ist " + calculationResult + ".";
    sendEmail(defaultSender, defaultRecipient, subject, mailBody);
  }

  /**
   * Send a single email.
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
      // message.setFrom( new InternetAddress(aFromEmailAddr) );
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
//    properties.put("mail.smtp.ssl.enable", "true");
    properties.put("mail.smtp.auth", "true");
    // properties.put("mail.smtp.user",
    // iboSmtpServerProperties.getProperty("ibo.smtp.server.username"));

    return properties;
  }

}
