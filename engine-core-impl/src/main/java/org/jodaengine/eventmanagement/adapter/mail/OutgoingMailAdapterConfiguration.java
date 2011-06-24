package org.jodaengine.eventmanagement.adapter.mail;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;

import org.jodaengine.eventmanagement.AdapterManagement;
import org.jodaengine.eventmanagement.adapter.EventAdapter;

/**
 * The configuration for our outgoing mail adapter with the necessary information to send an email via SMTP.
 */
public class OutgoingMailAdapterConfiguration extends AbstractMailConfiguration {
    
    /** The protocol. */
    private MailProtocol protocol;
    
    private String smtpServer;
    
    /** The port. */
    private int port;

    /**
     * Instantiates a new outgoing mail adapter configuration.
     *
     * @param protocol the protocol
     * @param userName the user name
     * @param password the password
     * @param address the address
     * @param smtpServer the SMTP server to use for sending the emails
     * @param port the port
     */
    public OutgoingMailAdapterConfiguration(@Nonnull MailProtocol protocol,
                                            @Nonnull String userName,
                                            @Nonnull String password,
                                            @Nonnull String address,
                                            @Nonnull String smtpServer,
                                            @Nonnegative int port) {

        super(userName, password, address);
        this.protocol = protocol;
        this.port = port;
        this.smtpServer = smtpServer;
        
    }

    /**
     * Gets the protocol used (will mostly be SMTP).
     *
     * @return the protocol
     */
    public MailProtocol getProtocol() {
        
        return protocol;
    }

    
    /**
     * Gets the port used, the default SMTP port is 25.
     *
     * @return the port
     */
    public int getPort() {
    
        return port;
    }
    
    /**
     * Gets the smtp server adress.
     *
     * @return the smtp server adress
     */
    public String getSmtpServer() {
        
        return smtpServer;
    }
    
    /**
     * Creates the outgoing Mail adapter.
     *
     * @return the outgoing mail adapter
     */
    private OutgoingMailAdapter createAdapter() {
   
        return new OutgoingMailAdapter(this);
         
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public EventAdapter registerAdapter(AdapterManagement adapterRegistrar) {

        OutgoingMailAdapter outgoingMailAdapter = this.createAdapter();
        adapterRegistrar.registerAdapter(outgoingMailAdapter);
        return outgoingMailAdapter;
    }

    @Override
    public int hashCode() {

        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + port;
        result = prime * result + ((protocol == null) ? 0 : protocol.hashCode());
        result = prime * result + ((smtpServer == null) ? 0 : smtpServer.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {

        if (this == obj) {
            return true;
        }
        if (!super.equals(obj)) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        OutgoingMailAdapterConfiguration other = (OutgoingMailAdapterConfiguration) obj;
        if (port != other.getPort()) {
            return false;
        }
        if (protocol != other.getProtocol()) {
            return false;
        }
        if (smtpServer == null) {
            if (other.smtpServer != null) {
                return false;
            }
        } else if (!smtpServer.equals(other.getSmtpServer())) {
            return false;
        }
        return true;
    }

}
