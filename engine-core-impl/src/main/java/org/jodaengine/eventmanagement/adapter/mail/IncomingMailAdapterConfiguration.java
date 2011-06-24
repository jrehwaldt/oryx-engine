package org.jodaengine.eventmanagement.adapter.mail;

import java.util.Properties;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;

import org.jodaengine.eventmanagement.AdapterManagement;
import org.jodaengine.eventmanagement.adapter.EventAdapter;
import org.jodaengine.eventmanagement.adapter.incoming.IncomingPullAdapter;
import org.jodaengine.eventmanagement.timing.QuartzPullAdapterConfiguration;
import org.jodaengine.eventmanagement.timing.job.PullAdapterJob;
import org.quartz.Job;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * The mail adapter configuration.
 */
public final class IncomingMailAdapterConfiguration extends AbstractMailConfiguration implements
QuartzPullAdapterConfiguration {

    private final int port;
    private final boolean useSSL;
    private final MailProtocol protocol;
    private static final long DEFAULT_INTERVAL = 6000L;
    private final Logger logger = LoggerFactory.getLogger(getClass());
    
    /**
     * Default constructor.
     * 
     * @param protocol
     *            the mail protocol
     * @param userName
     *            the account's user name
     * @param password
     *            the account's password
     * @param address
     *            the mail server's address
     * @param port
     *            the mail server's port
     * @param useSSL
     *            should ssl be used for connection
     */
    public IncomingMailAdapterConfiguration(@Nonnull MailProtocol protocol,
                                           @Nonnull String userName,
                                           @Nonnull String password,
                                           @Nonnull String address,
                                           @Nonnegative int port,
                                           @Nonnull boolean useSSL) {

        super(userName, password, address);
        this.protocol = protocol;
        this.port = port;
        this.useSSL = useSSL;
    }

    /**
     * Returns the port number.
     * 
     * @return the port number
     */
    public @Nonnegative
    int getPort() {

        return port;
    }

    /**
     * Creates a property set configured with the corresponding
     * settings.
     * 
     * @return a properties instance
     */
    public @Nonnull
    Properties toMailProperties() {

        // create the properties for the Session
        Properties props = new Properties();

        String identifier = "";
        switch (this.protocol) {
            case IMAP:
                identifier = "imap";
                break;
            case POP3:
                identifier = "pop3";
                break;
            default:
                logger.error("No supported mail type configured: {}", this.protocol);
        }

        if (this.useSSL) {
            // set this session up to use SSL for IMAP connections
            props.setProperty("mail." + identifier + ".socketFactory.class", "javax.net.ssl.SSLSocketFactory");
            // don't fallback to normal IMAP connections on failure.
            props.setProperty("mail." + identifier + ".socketFactory.fallback", "false");
        }

        // use the simap port for imap/ssl connections.
        props.setProperty("mail." + identifier + ".socketFactory.port", String.valueOf(this.port));
        props.setProperty("mail." + identifier + ".port", String.valueOf(this.port));

        // note that you can also use the defult imap port (including the
        // port specified by mail.imap.port) for your SSL port configuration.
        // however, specifying mail.imap.socketFactory.port means that,
        // if you decide to use fallback, you can try your SSL connection
        // on the SSL port, and if it fails, you can fallback to the normal
        // IMAP port.

        return props;
    }

    @Override
    public long getTimeInterval() {

        return DEFAULT_INTERVAL;
    }

    /**
     * Returns, whether to use ssl.
     * 
     * @return should use ssl
     */
    public boolean isUseSSL() {

        return useSSL;
    }

    /**
     * Returns the account's protocol.
     * 
     * @return the account protocol
     */
    public MailProtocol getProtocol() {

        return protocol;
    }

    @Override
    public String getUniqueName() {

        return String.format("%s:%s:%s:%s", protocol, domainName, port, userName);
    }

    /**
     * JodaEngine google configuration. This is to prevent code duplication. Remove this later.
     * 
     * @return the mail adapter configuration
     */
    public static IncomingMailAdapterConfiguration jodaGoogleConfiguration() {

        // TODO @All: WTF delete this (in July). Other options would be a local file.. but well. no.
// CHECKSTYLE:OFF 
        return new IncomingMailAdapterConfiguration(MailProtocol.IMAP, "oryxengine", "dalmatina!",
            "imap.googlemail.com", MailProtocol.IMAP.getPort(true), true);
// CHECKSTYLE:ON
    }

    @Override
    public Class<? extends Job> getScheduledClass() {

        return PullAdapterJob.class;
    }

    /**
     * create the Adapter which is defined by this Adapter configuration.
     * 
     * @return the InboundMailAdapter
     */
    private IncomingImapMailAdapter createAdapter() {

        IncomingImapMailAdapter adapter = new IncomingImapMailAdapter(this);
        logger.debug("Registered mail adapter {}", adapter);
        return adapter;
    }

    @Override
    public EventAdapter registerAdapter(AdapterManagement adapterRegistrar) {

        IncomingPullAdapter adapter = createAdapter();
        adapterRegistrar.registerIncomingPullAdapter(adapter);

        return adapter;
    }

    @Override
    public boolean pullingOnce() {

        return false;
    }

    @Override
    public int hashCode() {

        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + port;
        result = prime * result + ((protocol == null) ? 0 : protocol.hashCode());
        result = prime * result + (useSSL ? 1231 : 1237);
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
        IncomingMailAdapterConfiguration other = (IncomingMailAdapterConfiguration) obj;
        if (port != other.getPort()) {
            return false;
        }
        if (protocol != other.getProtocol()) {
            return false;
        }
        if (useSSL != other.isUseSSL()) {
            return false;
        }
        return true;
    }
    
}
